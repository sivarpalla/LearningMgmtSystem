package com.mks.lms.service;

import com.mks.lms.entity.RoleEntity;
import com.mks.lms.entity.UserEntity;
import com.mks.lms.model.User;
import com.mks.lms.repository.UserManagementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserManagementRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (Optional.ofNullable(user).isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + email);
        } else {

          Set<RoleEntity> roleEntities=new HashSet<>();
          roleEntities.add(user.getRole());
            List<GrantedAuthority> authorities = getUserAuthority(roleEntities);
            return  new UserDetailsImpl(user, authorities);
        }
    }
    private List<GrantedAuthority> getUserAuthority(Set<RoleEntity> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        for (RoleEntity  role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

   /* private UserDetails buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                user.isEnabled(), true, true, true, authorities);
    }*/

}