package com.amore.example.cache.domain;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@SequenceGenerator(name = "CATEGORY_NO_GEN", sequenceName = "CNG", initialValue = 11, allocationSize = 1)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_NO_GEN")
    private Long categoryNo;

    @Column(nullable = false)
    private String categoryName;

    @Nullable
    private Long parentNo;

    @Column(nullable = false)
    private Long depth;
}

