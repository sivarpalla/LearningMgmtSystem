package com.mks.lms.service;

import com.mks.lms.entity.RoleEntity;
import com.mks.lms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
        @Autowired
        private RoleRepository repository;

        @Override
        public RoleEntity findByName(String name) {
            RoleEntity role = repository.findByRoleName(name);
            return role;
        }
    }
