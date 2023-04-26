package com.mks.lms.repository;

import java.util.List;

import com.mks.lms.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserEntity, Integer> {

	@Query(nativeQuery = true,value = "SELECT user_id FROM mks_kms_usermgmt u WHERE lower(u.user_name) LIKE lower(concat('%',?1,'%'))  OR lower(u.user_email) LIKE lower(concat('%',?1,'%'))")
	List<Integer> searchByKeyword(String keyword);
	UserEntity findByEmail(String emial);

	Boolean existsByEmail(String email);

}
