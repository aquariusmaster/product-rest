package com.example.demo.service.handlers;

import com.example.demo.domain.MeasureUnit;
import com.example.demo.entity.Product;
import com.example.demo.exception.ParsingProductException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.BatchFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class XLSBatchHandler extends BatchFileHandler {
    private static final int BATCH_SIZE = 100;

    private final ProductRepository repository;

    @Override
    public boolean canHandle(MultipartFile file) {
        var originalFilename = file.getOriginalFilename();
        return originalFilename != null && (originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"));
    }

    public void process(MultipartFile file) {
        Objects.requireNonNull(file);
        log.info("Starting processing file: {}, size: {} (in bytes)", file.getOriginalFilename(), file.getSize());
        long startTime = nanoTime();
        try (InputStream is = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            var sheet = workbook.getSheetAt(0);
            long totalRowsProcessed = 0;

            var rowIterator = sheet.iterator();
            skipFirstRow(rowIterator);
            var products = new ArrayList<Product>(BATCH_SIZE);
            while (rowIterator.hasNext()) {
                var row = rowIterator.next();
                var product = mapRowToProduct(row);
                products.add(product);
                if (products.size() == BATCH_SIZE) {
                    if (log.isTraceEnabled()) {
                        log.trace("Saving part of products to the DB. Current row number: {}", row.getRowNum());
                    }
                    repository.saveAll(products);
                    products.clear();
                    totalRowsProcessed += BATCH_SIZE;
                }
            }
            if (!products.isEmpty()) {
                repository.saveAll(products);
                totalRowsProcessed += products.size();
            }
            log.info("Finished processing file: {}, total rows processed: {}, elapsed time: {} ms",
                    file.getOriginalFilename(),
                    totalRowsProcessed,
                    NANOSECONDS.toMillis(nanoTime() - startTime));
        } catch (ParsingProductException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing file: {}", file.getName(), e);
            throw new ParsingProductException("Error processing file:", e);
        }
    }


    private Product mapRowToProduct(Row row) {
        try {
            return Product.builder()
                    .name(row.getCell(0).getStringCellValue())
                    .price(BigDecimal.valueOf(row.getCell(1).getNumericCellValue()))
                    .unit(MeasureUnit.forString(row.getCell(2).getStringCellValue()))
                    .description(row.getCell(3).getStringCellValue())
                    .build();
        } catch (Exception e) {
            log.error("Error parsing row: {}", row.getRowNum(), e);
            throw new ParsingProductException("Error parsing row", e);
        }
    }

    private void skipFirstRow(Iterator<Row> rowIterator) {
        if (rowIterator.hasNext()) {
            rowIterator.next();
        } else {
            throw new IllegalStateException("Document is empty");
        }
    }

}
