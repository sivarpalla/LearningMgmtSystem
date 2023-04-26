package com.mks.lms.service;

import com.mks.lms.entity.UserEntity;
import com.mks.lms.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
	  private static final long serialVersionUID = 1L;
	  private  UserEntity user;

	  UserDetailsImpl(UserEntity user,List<GrantedAuthority> authorities){
		  this.user=user;
		  this.authorities=authorities;

	  }

	  private Collection<? extends GrantedAuthority> authorities;


	  @Override
	public String getUsername() {
		return user.getEmail();
	}
	@Override
	public boolean isEnabled(){
		return   user.isEnabled();
	}


	@Override
	public String getPassword(){
		  return user.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}