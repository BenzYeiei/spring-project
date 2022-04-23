package com.service.backend.Service.Resource.Account;

import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Repository.Account.AccountRepository;
import com.service.backend.JPA.Entity.Account.RoleEntity;
import com.service.backend.JPA.Repository.Account.RoleRepository;
import com.service.backend.Service.ServiceModel.AccountService.InitRegisterModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InitRegisterService implements InitRegisterModel {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void createRole(RoleEntity roleEntity) {

        // check duplicate of role name
        boolean isRoleName = roleRepository.existsByRoleName(roleEntity.getRoleName());
        if (isRoleName) {
            log.info("Role {} created.", roleEntity.getRoleName());
            return;
        }

        // save role
        RoleEntity roleEntityResult = roleRepository.save(roleEntity);

        // check role after save
        if (roleEntityResult.getRoleName() == null) {
            log.error("can't create {}", roleEntity.getRoleName());
        }
    }


    @Override
    public RoleEntity getOncRole(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<RoleEntity> getListRole() {
        return roleRepository.findAll();
    }

    @Override
    public void createAdmin(AccountEntity accountEntity) {
        // check email is exist
        boolean isEmail = accountRepository.existsByUsername(accountEntity.getUsername());
        if (isEmail) {
            log.info("{}. -> created.", accountEntity.getUsername());
            return;
        }

        // encode password
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));

        // save
        AccountEntity accountEntityResult = accountRepository.save(accountEntity);

        // check account after save
        if (accountEntityResult.getUsername() == null) {
            log.error("can't create {}.", accountEntity.getUsername());
        }
    }
}
