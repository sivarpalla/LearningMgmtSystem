package com.mks.lms.service;

import com.mks.lms.entity.LmsDepartmentEntity;
import com.mks.lms.model.DepartmentRequest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DepartmentService {
    void saveDepartment(DepartmentRequest departmentRequest);
    @Transactional
    void updateDepartment(DepartmentRequest departmentRequest,int departmentId);

    void deleteDepartment(int departmentId);

     Page<LmsDepartmentEntity> getProducts(int pageNumber, int pageSize);
    LmsDepartmentEntity findByDepartmentName(String departmentName);

     LmsDepartmentEntity findById(int departmentId);
     void readExcelDataToDB(MultipartFile readDataFile);
}
