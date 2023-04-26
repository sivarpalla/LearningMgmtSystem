package com.mks.lms.service;

import com.mks.lms.entity.LmsCategoryEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.CategoryRequestModel;
import com.mks.lms.repository.LmsCategoryRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    LmsCategoryRepository lmsCategoryRepository;
    @Override
    @Transactional
    public void addCategory(CategoryRequestModel categoryRequestModel){
        try{
            LmsCategoryEntity lmsCategoryEntity = LmsCategoryEntity.builder()
                    .categoryName(categoryRequestModel.getCategoryName())
                    .categoryDescription(categoryRequestModel.getCategoryDescription()).build();
            lmsCategoryRepository.save(lmsCategoryEntity);
        } catch (Exception e) {
            log.error("Error processing the add category name " + categoryRequestModel.getCategoryName());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public void editCategory(int categoryId, CategoryRequestModel categoryRequestModel){
        try{
            Optional<LmsCategoryEntity> optionalLmsCategory = lmsCategoryRepository.findById(categoryId);
            if(optionalLmsCategory.isPresent()){
                LmsCategoryEntity lmsCategoryEntity = optionalLmsCategory.get();
                lmsCategoryEntity.setCategoryName(categoryRequestModel.getCategoryName());
                lmsCategoryEntity.setCategoryDescription(categoryRequestModel.getCategoryDescription());

                lmsCategoryRepository.save(lmsCategoryEntity);
            }
        } catch (Exception e) {
            log.error("Error processing the edit category name " + categoryRequestModel.getCategoryName());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public void deleteCategory(int categoryId) {
        lmsCategoryRepository.deleteById(categoryId);
    }
    @Override
    public LmsCategoryEntity findByCategoryName(String categoryName) {
        return lmsCategoryRepository.findByCategoryName(categoryName);
    }
    @Override
    public Page<LmsCategoryEntity> getCategories(int pageNumber, int pageSize) {
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return lmsCategoryRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error processing the get categories page number " + pageNumber);
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public void readExcelDataToDB(MultipartFile readDataFile){
        try{
            List<LmsCategoryEntity> entityList = new ArrayList<LmsCategoryEntity>();
            LmsCategoryEntity lmsCategoryEntity;
            XSSFWorkbook workbook = new XSSFWorkbook(readDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                XSSFRow row = worksheet.getRow(i);
                lmsCategoryEntity = LmsCategoryEntity.builder()
                        .categoryName(row.getCell(0).getStringCellValue())
                        .categoryDescription(row.getCell(1).getStringCellValue()).build();
                entityList.add(lmsCategoryEntity);
            }
            lmsCategoryRepository.saveAll(entityList);
        } catch (IOException e) {
            log.error("Error processing the read excel account data, message {} ",e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public Boolean checkDuplicateRecord(String name){
        try{
            LmsCategoryEntity lmsCategoryEntity = findByCategoryName(name.trim());
            if(Optional.ofNullable(lmsCategoryEntity).isPresent()){
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error processing the check Duplicate Record on name " + name);
            throw new ServiceException(e.getMessage());
        }
    }
}
