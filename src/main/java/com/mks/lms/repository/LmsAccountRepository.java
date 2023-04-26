package com.mks.lms.repository;

import com.mks.lms.entity.LmsAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LmsAccountRepository extends JpaRepository<LmsAccountEntity, Integer> {

    LmsAccountEntity findByAccountName(String accountName);

}

