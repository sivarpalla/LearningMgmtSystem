package com.mks.lms.model;

import com.mks.lms.entity.LmsAccountEntity;
import com.mks.lms.entity.LmsDepartmentEntity;
import com.mks.lms.entity.RoleEntity;
import com.mks.lms.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private  long userId;
    private  String userName;
    private String email;
    private String password;
    private String department ;
    private String accountType;
    private  String role=UserRole.USER.name();
    private boolean enabled;


    public static User map(UserEntity u) {
        return User.builder().accountType(Optional.ofNullable(u.getAccount()).orElseGet(LmsAccountEntity::new).getAccountName())
                .department(Optional.ofNullable(u.getDepartment()).orElseGet(LmsDepartmentEntity::new).getDepartmentName())
                .role(Optional.ofNullable(u.getRole()).orElseGet(RoleEntity::new).getRoleName())
                .email(u.getEmail()).userId(u.getUserId()).userName(u.getUserName())
                .build();
    }


}
