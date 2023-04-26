package com.mks.lms.service;

import com.mks.lms.entity.LmsDepartmentEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.DepartmentRequest;
import com.mks.lms.repository.DepartmentRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void saveDepartment(DepartmentRequest departmentRequest) {
        if (departmentRequest != null) {
            LmsDepartmentEntity departmentEntity = LmsDepartmentEntity.builder()
                    .departmentName(departmentRequest.departmentName)
                    .departmentDescription(departmentRequest.getDepartmentDescription())
                    .build();
            departmentRepository.save(departmentEntity);
        }
    }

    @Override
    @Transactional
    public void updateDepartment(DepartmentRequest departmentRequest,int departmentId) {
        LmsDepartmentEntity lmsDepartmentEntity = departmentRepository.findById(departmentId).get();
        if(Optional.of(lmsDepartmentEntity).isPresent()){
            lmsDepartmentEntity.setDepartmentName(departmentRequest.getDepartmentName());
            lmsDepartmentEntity.setDepartmentDescription(departmentRequest.getDepartmentDescription());
        }
        departmentRepository.save(lmsDepartmentEntity);
    }

    @Override
    public void deleteDepartment(int departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public Page<LmsDepartmentEntity> getProducts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return departmentRepository.findAll(pageable);
    }

    @Override
    public LmsDepartmentEntity findByDepartmentName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName);
    }

    @Override
    public LmsDepartmentEntity findById(int departmentId) {
        return departmentRepository.findById(departmentId).get();
    }

    @Override
    @Transactional
    public void readExcelDataToDB(MultipartFile readDataFile) {
        try {
            List<LmsDepartmentEntity> entityList = new ArrayList<LmsDepartmentEntity>();
            LmsDepartmentEntity lmsDepartmentEntity = null;
            XSSFWorkbook workbook = new XSSFWorkbook(readDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                lmsDepartmentEntity = LmsDepartmentEntity.builder()
                        .departmentName(row.getCell(0).getStringCellValue())
                        .departmentDescription(row.getCell(1).getStringCellValue()).build();
                entityList.add(lmsDepartmentEntity);
            }
            departmentRepository.saveAll(entityList);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
