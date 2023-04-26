package com.mks.lms.controller;

import com.mks.lms.constants.LmsConstants;
import com.mks.lms.entity.LmsCategoryEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.CategoryRequestModel;
import com.mks.lms.model.response.MessageResponse;
import com.mks.lms.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lms/category")
@Slf4j
public class CategoryController {

    private static final String NO_CONTENT_MSG = "No content found";
    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/add",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addCategory(
            @RequestBody CategoryRequestModel categoryRequestModel) {
        log.info("CategoryController add category request starts");
        if(categoryRequestModel == null || StringUtils.isBlank(categoryRequestModel.getCategoryName())){
            log.error("CategoryController add category request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            if (categoryService.checkDuplicateRecord(categoryRequestModel.getCategoryName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Category name already exist!"));
            }
            categoryService.addCategory(categoryRequestModel);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (ServiceException e) {
            log.error("Exception while adding category data for Category name {} message {} ", categoryRequestModel.getCategoryName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/edit/{categoryId}",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity editCategory(@PathVariable("categoryId") int categoryId,
            @RequestBody CategoryRequestModel categoryRequestModel) {
        log.info("CategoryController edit category request starts");
        if(categoryRequestModel == null || categoryId <=0
        || StringUtils.isBlank(categoryRequestModel.getCategoryName())){
            log.error("CategoryController edit category request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            categoryService.editCategory(categoryId, categoryRequestModel);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ServiceException e) {
            log.error("Exception while editing category data for Category name {} message {} ", categoryRequestModel.getCategoryName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity deleteCategoryById(@PathVariable("categoryId") int categoryId) {
        log.info("CategoryController delete category request starts");
        if(categoryId <=0){
            log.error("CategoryController delete category request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ServiceException e) {
            log.error("Exception while deleting category data for category Id {} message {} ", categoryId,e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{categoryName}")
    public ResponseEntity<LmsCategoryEntity> findByCategoryName(@PathVariable("categoryName") String categoryName) {
        log.info("CategoryController get category name request starts");
        if(StringUtils.isBlank(categoryName)){
            log.error("CategoryController get category name request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            return Optional
                    .ofNullable(categoryService.findByCategoryName(categoryName))
                    .map( categories -> ResponseEntity.ok().body(categories) )
                    .orElseGet( () -> ResponseEntity.notFound().build() );
        } catch (ServiceException e) {
            log.error("Exception while getting category name  {} message {}", categoryName, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);

        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Page<LmsCategoryEntity>> getCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try{
            log.info("CategoryController get categories request starts");
            Page<LmsCategoryEntity> categories = categoryService.getCategories(pageNumber, pageSize);
            return ResponseEntity.ok(categories);
        } catch (ServiceException e) {
            log.error("Exception while getting categories page  {} message {}", pageNumber, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);

        }
    }

    @PostMapping(value = "/categoryDataImport",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity readExcelDatatoDB(@RequestParam("file") MultipartFile readDataFile) {
        try{
            String message="";
            log.info("CategoryController reading excel account data request starts");
            if(readDataFile == null ){
                log.error("CategoryController reading excel account data request, No content found");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File Not found !"));
            }
            if(!LmsConstants.hasExcelFormat(readDataFile)){
                log.error("CategoryController reading excel account data request, No content found");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Could not upload the file: !"));
            }
            categoryService.readExcelDataToDB(readDataFile);
            message = "Uploaded the file successfully: " + readDataFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ServiceException e) {
            log.error("Exception while reading excel account data, message {}", e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
