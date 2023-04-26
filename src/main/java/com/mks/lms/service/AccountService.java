package com.mks.lms.service;

import com.mks.lms.entity.LmsAccountEntity;
import com.mks.lms.model.AccountRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AccountService {

     void addAccount(AccountRequestModel accountRequestModel);
     void editAccount(int accountID, AccountRequestModel accountRequestModel);
     void deleteAccount(int accountId) ;
     LmsAccountEntity findByAccountName(String accountName) ;
     Page<LmsAccountEntity> getAccounts(int pageNumber, int pageSize) ;
     void readExcelDataToDB(MultipartFile readDataFile);
     Boolean checkDuplicateRecord(String name);
}
