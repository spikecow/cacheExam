package com.amore.example.cache.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@SequenceGenerator(name = "PRODUCT_NO_GEN", sequenceName = "PNG", initialValue = 1001, allocationSize = 1)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_NO_GEN")
    private Long productNo;

    private String brandName;
    private String productName;

    @Column(precision = 19, scale = 2)
    private BigDecimal productPrice;

    private Long categoryNo;
}

