package com.mks.lms.service;

import com.mks.lms.entity.LmsAccountEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.AccountRequestModel;
import com.mks.lms.repository.LmsAccountRepository;
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
public class AccountServiceImpl implements AccountService{

    @Autowired
    LmsAccountRepository lmsAccountRepository;
    @Override
    @Transactional
    public void addAccount(AccountRequestModel accountRequestModel){
        try{
        LmsAccountEntity lmsAccountEntity = LmsAccountEntity.builder()
                .accountName(accountRequestModel.getAccountName())
                .accountDescription(accountRequestModel.getAccountDescription()).build();
        lmsAccountRepository.save(lmsAccountEntity);
        } catch (Exception e) {
            log.error("Error processing the add account name " + accountRequestModel.getAccountName());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public void editAccount(int accountID, AccountRequestModel accountRequestModel){
        try{
        Optional<LmsAccountEntity> optionalLmsAccount = lmsAccountRepository.findById(accountID);
        if(optionalLmsAccount.isPresent()){
            LmsAccountEntity lmsAccountEntity = optionalLmsAccount.get();
            lmsAccountEntity.setAccountName(accountRequestModel.getAccountName());
            lmsAccountEntity.setAccountDescription(accountRequestModel.getAccountDescription());

            lmsAccountRepository.save(lmsAccountEntity);
        }
        } catch (Exception e) {
            log.error("Error processing the edit account name " + accountRequestModel.getAccountName());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public void deleteAccount(int accountId) {
        lmsAccountRepository.deleteById(accountId);
    }
    @Override
    public LmsAccountEntity findByAccountName(String accountName) {
        return lmsAccountRepository.findByAccountName(accountName);
    }
    @Override
    public Page<LmsAccountEntity> getAccounts(int pageNumber, int pageSize) {
        try{
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return lmsAccountRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error processing the get accounts page number " + pageNumber);
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public void readExcelDataToDB(MultipartFile readDataFile){
        try{
            List<LmsAccountEntity> entityList = new ArrayList<LmsAccountEntity>();
            LmsAccountEntity lmsAccountEntity;
            XSSFWorkbook workbook = new XSSFWorkbook(readDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                XSSFRow row = worksheet.getRow(i);
                 lmsAccountEntity = LmsAccountEntity.builder()
                        .accountName(row.getCell(0).getStringCellValue())
                        .accountDescription(row.getCell(1).getStringCellValue()).build();
                entityList.add(lmsAccountEntity);
            }
            lmsAccountRepository.saveAll(entityList);
        } catch (IOException  e) {
            log.error("Error processing the read excel account data, message {} ",e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    @Transactional
    public Boolean checkDuplicateRecord(String name){
        try{
            LmsAccountEntity lmsAccountEntity = findByAccountName(name.trim());
            if(Optional.ofNullable(lmsAccountEntity).isPresent()){
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error processing the check Duplicate Record on name " + name);
            throw new ServiceException(e.getMessage());
        }
    }
}
