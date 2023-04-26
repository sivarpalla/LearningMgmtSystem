package com.mks.lms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.mks.lms.constants.LmsConstants;
import com.mks.lms.entity.LmsAccountEntity;
import com.mks.lms.entity.LmsDepartmentEntity;
import com.mks.lms.entity.RoleEntity;
import com.mks.lms.entity.UserEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.exceptions.UserFoundException;
import com.mks.lms.model.User;
import com.mks.lms.model.ValidateUser;
import com.mks.lms.model.request.ChangePasswordRequest;
import com.mks.lms.model.request.SignupRequest;
import com.mks.lms.model.response.ChangePasswordResponse;
import com.mks.lms.repository.UserManagementRepository;
import com.mks.lms.constants.LmsConstants;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	UserManagementRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RoleService roleService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	AccountService accountService;




	@Override
	public List<User> getAllUsers(int page, int size) {
		Pageable sortedById = PageRequest.of(page, size, Sort.by("userId"));
		log.info("getAllUsers page:{} and  size:{}", page, size);

		List<UserEntity> userList = userRepository.findAll(sortedById).getContent();
		return userList.stream().map(User::map).collect(Collectors.toList());
	}

	@Override
	public User getUserById(Integer userEntityId) {
		log.info("getUserById UserEntityId {}", userEntityId);
		return userRepository.findById(userEntityId).map(User::map).orElseThrow(() -> new ServiceException("UserEntity not found"));
	}

	@Override
	public List<User> searchByKeyword(String keyword) {
		log.info("searchByKeyword keyword {}", keyword);
		List<Integer> idList = userRepository.searchByKeyword(keyword);
		return idList.stream().map(id->getUserById(id)).collect(Collectors.toList());
		//return userList.stream().map(User::map).collect(Collectors.toList());
	}

	@Override
	public UserEntity createUser(User user) {
		log.info("createUser  {}", user);
		UserEntity exitingUser= userRepository.findByEmail(user.getEmail());
		if(Optional.ofNullable(exitingUser).isPresent()) {
			throw new UserFoundException(user.getEmail());
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		UserEntity userEntity=modelMapper.map(user,UserEntity.class);
		RoleEntity role = roleService.findByName(user.getRole());
		/*Set<RoleEntity> roleEntitySet = new HashSet<>();
		roleEntitySet.add(role);*/
		userEntity.setRole(role);
		LmsDepartmentEntity lmsDepartmentEntity=departmentService.findByDepartmentName(user.getDepartment());
		userEntity.setDepartment(lmsDepartmentEntity);
		LmsAccountEntity lmsAccountEntity=accountService.findByAccountName(user.getAccountType());
		userEntity.setAccount(lmsAccountEntity);
		if(Optional.ofNullable(lmsDepartmentEntity).isEmpty() || Optional.ofNullable(lmsAccountEntity).isEmpty()) {
			log.error("createUser Invalid Account or Department:{} - lmsAccountEntity:{}",lmsDepartmentEntity,lmsAccountEntity);
			throw new ServiceException(LmsConstants.INVALID);
		}
		return   userRepository.save(userEntity);
	}

	@Override
	public UserEntity updateUser(Integer userEntityId, User user) {
		UserEntity userEntity = userRepository.findById(userEntityId)
				.orElseThrow(() -> new ServiceException("UserEntity not found"));

		RoleEntity role = roleService.findByName(user.getRole());
		/*Set<RoleEntity> roleEntitySet = new HashSet<>();
		roleEntitySet.add(role);*/
		userEntity.setRole(role);
		LmsDepartmentEntity lmsDepartmentEntity=departmentService.findByDepartmentName(user.getDepartment());
		if(!user.getDepartment().equalsIgnoreCase(userEntity.getDepartment().getDepartmentName())){
			userEntity.setDepartment(lmsDepartmentEntity);
		}
		LmsAccountEntity lmsAccountEntity=accountService.findByAccountName(user.getAccountType());
		if(!user.getAccountType().equalsIgnoreCase(userEntity.getAccount().getAccountName())){
			userEntity.setAccount(lmsAccountEntity);
		}
		if(Optional.ofNullable(lmsDepartmentEntity).isEmpty() || Optional.ofNullable(lmsAccountEntity).isEmpty()) {
			log.error("updateUser Invalid Account or Department:{} - lmsAccountEntity:{}",lmsDepartmentEntity,lmsAccountEntity);
			throw new ServiceException(LmsConstants.INVALID);
		}
		return userRepository.save(userEntity);
	}

	@Override
	public void deleteUser(Integer userEntityId) {
		UserEntity userEntity = userRepository.findById(userEntityId)
				.orElseThrow(() -> new ServiceException("UserEntity not found"));
		userRepository.delete(userEntity);
	}

	@Override
	public boolean validateUser(ValidateUser user) {
		UserEntity retrievedUsed = userRepository.findById((int)user.getUserId())
				.orElseThrow(() -> new ServiceException("UserEntity not found"));
		boolean isMatch = bCryptPasswordEncoder.matches(user.getEmail(), retrievedUsed.getUserName());
		log.info("validateUser UserEntityId {} and isMatch-{}", user.getUserId(), isMatch);
		return isMatch;
	}

	@Override
	public Boolean existsByEmail(String email) {
		log.info("check existing user {}", email);
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserEntity createUser(SignupRequest user) {
		log.info("createUser  {}", user);
		UserEntity exitingUser= userRepository.findByEmail(user.getEmail());
		if(Optional.ofNullable(exitingUser).isPresent()) {
			throw new UserFoundException(user.getEmail());
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		UserEntity userEntity=modelMapper.map(user,UserEntity.class);
		RoleEntity role = roleService.findByName(user.getRole());
		/*Set<RoleEntity> roleEntitySet = new HashSet<>();
		roleEntitySet.add(role);*/
		userEntity.setRole(role);
		LmsDepartmentEntity lmsDepartmentEntity=departmentService.findByDepartmentName(user.getDepartment());
		userEntity.setDepartment(lmsDepartmentEntity);
		LmsAccountEntity lmsAccountEntity=accountService.findByAccountName(user.getAccountType());
		userEntity.setAccount(lmsAccountEntity);
		if(Optional.ofNullable(lmsDepartmentEntity).isEmpty() || Optional.ofNullable(lmsAccountEntity).isEmpty()) {
			log.error("Invalid Account or Department:{} - lmsAccountEntity:{}",lmsDepartmentEntity,lmsAccountEntity);
			throw new ServiceException(LmsConstants.INVALID);
		}
		return   userRepository.save(userEntity);
	}
	
	@Override
	@SneakyThrows
	@Transactional
	public void importUser(MultipartFile excel) {
		//Excel headers in the file (row 1): userName	email	department	accountType		role		
		XSSFWorkbook  workbook = new XSSFWorkbook(excel.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		 for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
		        XSSFRow row = worksheet.getRow(i);
		        SignupRequest user= SignupRequest.builder()
		        		 .userName(row.getCell(0).getStringCellValue())
		        		 .email(row.getCell(1).getStringCellValue())
		        		 .department(row.getCell(2).getStringCellValue())
		        		 .accountType(row.getCell(3).getStringCellValue())
		        		 .role(row.getCell(4).getStringCellValue())
		        		 .password(LmsConstants.DEFAULT_PASSWORD)
		        		 .enabled(true)
		        		 .build();
		         log.info("importUser user {}",user);
		         this.createUser(user);
		    }
	}

	@Override
	public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
		UserEntity user=userRepository.findByEmail(request.getEmail());

		if(Optional.ofNullable(user).isEmpty()){
			throw  new UserFoundException(request.getEmail());
		}
		if(bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())){
			user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
			userRepository.save(user);
			return  ChangePasswordResponse.builder().message("Password have changed successfully").status("Success").build();
		}else {
			return  ChangePasswordResponse.builder().message("Old password incorrect").status("Fail").build();
		}
	}
}