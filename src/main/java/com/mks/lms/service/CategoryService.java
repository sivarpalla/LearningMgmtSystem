package com.mks.lms.service;

import com.mks.lms.entity.LmsCategoryEntity;
import com.mks.lms.model.CategoryRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CategoryService {

     void addCategory(CategoryRequestModel categoryRequestModel);
     void editCategory(int categoryId, CategoryRequestModel categoryRequestModel);
     void deleteCategory(int categoryId) ;
     LmsCategoryEntity findByCategoryName(String categoryName) ;
     Page<LmsCategoryEntity> getCategories(int pageNumber, int pageSize) ;
    void readExcelDataToDB(MultipartFile readDataFile);
    Boolean checkDuplicateRecord(String name);
}
