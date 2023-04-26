package com.mks.lms.repository;

import com.mks.lms.entity.LmsCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LmsCategoryRepository extends JpaRepository<LmsCategoryEntity, Integer> {

    LmsCategoryEntity findByCategoryName(String categoryName);
}
