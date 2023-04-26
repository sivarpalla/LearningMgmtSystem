package com.mks.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mks.lms.entity.LmsContentEntity;

@Repository
public interface LmsContentRepository extends JpaRepository<LmsContentEntity, Integer> {
	List<LmsContentEntity> findByDeptIdAndAccountIdAndCategoryAndFileName(@Param("deptId") String deptId,@Param("accountId") String accountId,@Param("category") String category,@Param("fileName") String fileName);
	List<LmsContentEntity> findByFileName(String fname);
}
