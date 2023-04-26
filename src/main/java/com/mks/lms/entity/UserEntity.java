package com.mks.lms.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mks_kms_usermgmt", schema = "lms")
public class UserEntity {

	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@Column(name="user_name",nullable = false)
	private String userName;
	
	@Column(name="user_email",nullable = false,unique = true)
	private String email;
	
	@Column(name="user_pwd",nullable = false)
	private String password;

	private boolean enabled;
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id",unique = false)
	)
	private  RoleEntity role ;
	
	@OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "user_department",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "departmentId" ))
    private LmsDepartmentEntity department;

	@OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "user_account",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "accountId" ))
    private LmsAccountEntity account;
	
}
