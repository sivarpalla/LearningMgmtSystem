package com.mks.lms.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mks_kms_department", schema = "lms")
public class LmsDepartmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "departmentId")
    private Integer departmentId;

    @Column(name = "departmentName",nullable = false,unique = true)
    private String departmentName;
    @Column(name = "departmentDescription")
    private String departmentDescription;

}
