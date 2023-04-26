package com.mks.lms.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mks_kms_account")
public class LmsAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="accountId")
    private Integer accountId;

    @Column(name="accountName",nullable = false,unique = true)
    private String accountName;

    @Column(name="accountDescription")
    private String accountDescription;
}
