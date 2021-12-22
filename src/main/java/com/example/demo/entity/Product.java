package com.example.demo.entity;

import com.example.demo.domain.MeasureUnit;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

import static org.hibernate.id.SequenceGenerator.SEQUENCE;

@Builder
@Getter @Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence-generator"
    )
    @SequenceGenerator(
            name = "sequence-generator",
            sequenceName = "product_sequence",
            initialValue = 4
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private MeasureUnit unit;

    @Column(nullable = false)
    private String description;
}
