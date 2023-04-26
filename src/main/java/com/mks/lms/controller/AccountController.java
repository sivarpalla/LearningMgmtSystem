package com.mks.lms.controller;

import com.mks.lms.constants.LmsConstants;
import com.mks.lms.entity.LmsAccountEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.AccountRequestModel;
import com.mks.lms.model.response.MessageResponse;
import com.mks.lms.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;


@RestController
@RequestMapping("/api/lms/account")
@Slf4j
public class AccountController {

    private static final String NO_CONTENT_MSG = "No content found";
    @Autowired
    AccountService accountService;

    @PostMapping(value = "/add",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addAccount(
            @RequestBody AccountRequestModel accountRequestModel) {
        log.info("AccountController add account request starts");
        if(accountRequestModel == null || StringUtils.isBlank(accountRequestModel.getAccountName())){
            log.error("AccountController add account request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            if (accountService.checkDuplicateRecord(accountRequestModel.getAccountName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Account name already exist!"));
            }
            accountService.addAccount(accountRequestModel);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (ServiceException e) {
            log.error("Exception while adding account data for Account name {} message {} ", accountRequestModel.getAccountName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/edit/{accountID}",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity editAccount(@PathVariable("accountID") int accountID,
            @RequestBody AccountRequestModel accountRequestModel) {
        log.info("AccountController edit account request starts");
        if(accountRequestModel == null || accountID <=0
           || StringUtils.isBlank(accountRequestModel.getAccountName())){
            log.error("AccountController edit account request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
            accountService.editAccount(accountID, accountRequestModel);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ServiceException e) {
            log.error("Exception while editing account data for Account name {} message {} ", accountRequestModel.getAccountName(),e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity deleteAccountById(@PathVariable("accountId") int accountId) {
        log.info("AccountController delete account request starts");
        if(accountId <=0){
            log.error("AccountController delete account request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
                accountService.deleteAccount(accountId);
                return ResponseEntity.ok(HttpStatus.OK);
            } catch (ServiceException e) {
                log.error("Exception while deleting account data for Account Id {} message {} ", accountId,e.getMessage());
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
    @GetMapping("/{accountName}")
    public ResponseEntity<LmsAccountEntity> findByAccountName(@PathVariable("accountName") String accountName) {
        log.info("AccountController get account name request starts");
        if(StringUtils.isBlank(accountName)){
            log.error("AccountController get account name request, No content found");
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);
        }
        try{
        return Optional
                .ofNullable(accountService.findByAccountName(accountName))
                .map( accounts -> ResponseEntity.ok().body(accounts) )
                .orElseGet( () -> ResponseEntity.notFound().build() );
        } catch (ServiceException e) {
            log.error("Exception while getting Account name  {} message {}", accountName, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);

        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<Page<LmsAccountEntity>> getAccounts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try{
            log.info("AccountController get accounts request starts");
            Page<LmsAccountEntity> accounts = accountService.getAccounts(pageNumber, pageSize);
            return ResponseEntity.ok(accounts);
        } catch (ServiceException e) {
            log.error("Exception while getting Accounts page  {} message {}", pageNumber, e.getMessage());
            return new ResponseEntity(NO_CONTENT_MSG,HttpStatus.NO_CONTENT);

        }
    }
    @PostMapping(value = "/accountDataImport",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity readExcelDatatoDB(@RequestParam("file") MultipartFile readDataFile) {
        try{
            String message="";
            log.info("AccountController reading excel account data request starts");
            if(readDataFile == null ){
                log.error("AccountController reading excel account data request, No content found");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File Not found !"));
            }
            if(!LmsConstants.hasExcelFormat(readDataFile)){
                log.error("AccountController reading excel account data request, No content found");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Could not upload the file: !"));
            }
            accountService.readExcelDataToDB(readDataFile);
            message = "Uploaded the file successfully: " + readDataFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (ServiceException e) {
            log.error("Exception while reading excel account data, message {}", e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
