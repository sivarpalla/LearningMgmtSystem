package com.mks.lms.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mks_kms_category")
public class LmsCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="categoryId")
    private Integer categoryId;

    @Column(name="categoryName",nullable = false,unique = true)
    private String categoryName;

    @Column(name="categoryDescription")
    private String categoryDescription;
}
