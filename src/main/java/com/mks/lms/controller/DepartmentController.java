package com.mks.lms.controller;

import com.mks.lms.entity.LmsDepartmentEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.DepartmentRequest;
import com.mks.lms.service.DepartmentService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("api/lms/department")
@Slf4j
public class DepartmentController {

    private static final String NO_CONTENT_MSG = "No content found";

    @Autowired
    DepartmentService departmentService;

    @PostMapping(value = "/add",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addAccount(
            @RequestBody DepartmentRequest departmentRequest) {
        log.info("DepartmentController add department request starts");
        if(departmentRequest == null || StringUtils.isBlank(departmentRequest.getDepartmentName())){
            log.error("DepartmentController add department request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG, HttpStatus.NO_CONTENT);
        }
        try{
            departmentService.saveDepartment(departmentRequest);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (ServiceException e) {
            log.error("Exception while adding account data for Department name {} message {} ", departmentRequest.getDepartmentName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/edit/{departmentId}",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity editAccount(
            @PathVariable("departmentId") int departmentId,@RequestBody DepartmentRequest departmentRequest) {
        log.info("DepartmentController edit account request starts");
        if(departmentRequest == null || Optional.ofNullable(departmentRequest.getDepartmentName()).isEmpty()){
            log.error("DepartmentController edit account request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            departmentService.updateDepartment(departmentRequest,departmentId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ServiceException e) {
            log.error("Exception while editing department data for Account name {} message {} ", departmentRequest.getDepartmentName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<Page<LmsDepartmentEntity>> getAccounts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try{
            log.info("DepartmentController get department request starts");
            Page<LmsDepartmentEntity> accounts = departmentService.getProducts(pageNumber, pageSize);
            return ResponseEntity.ok(accounts);
        } catch (ServiceException e) {
            log.error("DepartmentController while getting department page  {} message {}", pageNumber, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);

        }
    }

    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity deleteAccountById(@PathVariable("departmentId") int departmentId) {
        log.info("DepartmentController delete department request starts");
        if(departmentId <=0){
            log.error("DepartmentController delete department request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            departmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ServiceException e) {
            log.error("Exception while deleting department data for Account Id {} message {} ", departmentId,e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<LmsDepartmentEntity> findById(@PathVariable("departmentId") int departmentId){
        try{
            LmsDepartmentEntity lmsDepartmentEntity = departmentService.findById(departmentId);
            return new ResponseEntity<>(lmsDepartmentEntity,HttpStatus.OK);
        }catch (ServiceException e){
            log.error("Exception while getting department name  {} message {}", departmentId, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping(value = "/departmentDataImport",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity readExcelDatatoDB(@RequestParam("file") MultipartFile readDataFile) {
        try{
            String message="";
            log.info("AccountController reading excel account data request starts");
            if(readDataFile == null ){
                log.error("AccountController reading excel account data request, No content found");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File Not found !");
            }
            departmentService.readExcelDataToDB(readDataFile);
            message = "Uploaded the file successfully: " + readDataFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ServiceException e) {
            log.error("Exception while reading excel account data, message {}", e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
