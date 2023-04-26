package com.mks.lms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "mks_lms_roles")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "role_name",nullable = false,unique = true)
    private String roleName;
}
