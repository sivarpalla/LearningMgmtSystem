package com.mks.lms.repository;

import com.mks.lms.entity.LmsDepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<LmsDepartmentEntity, Integer> {


    LmsDepartmentEntity findByDepartmentName(String departmentName);
}
