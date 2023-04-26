package com.mks.lms.repository;

import com.mks.lms.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByRoleName(String name);
}
