package com.example.demo.service;

import com.example.demo.domain.MeasureUnit;
import com.example.demo.entity.Product;
import com.example.demo.exception.ParsingProductException;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchProcessor implements BatchFileProcessorService {
    private static final int BATCH_SIZE = 100;

    private final ProductRepository repository;

    @Override
    public void process(MultipartFile file) {
        Objects.requireNonNull(file);
        log.info("Starting processing file: {}, size: {} (in bytes)", file.getOriginalFilename(), file.getSize());
        try (InputStream is = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            long totalRowsProcessed = 0;
            long startTime = nanoTime();

            Iterator<Row> rowIterator = sheet.iterator();
            skipFirstRow(rowIterator);
            List<Product> products = new ArrayList<>(BATCH_SIZE);
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Product product = mapRowToProduct(row);
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
