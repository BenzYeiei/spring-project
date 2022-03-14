package com.demo.firstProject.Service.Resource.Account;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.PrivilegeEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.Account.PrivilegeRepository;
import com.demo.firstProject.JPA.Repository.Account.RoleRepository;
import com.demo.firstProject.Service.ServiceModel.RegisterService.InitRegisterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitRegisterService implements InitRegisterModel {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void createRole(RoleEntity roleEntity) {
        // check duplicate of role name
        boolean isRoleName = roleRepository.existsByRoleName(roleEntity.getRoleName());
        if (isRoleName) {
            System.out.println("Role " + roleEntity.getRoleName() + " created.");
            return;
        }
        // save role
        RoleEntity roleEntityResult = roleRepository.save(roleEntity);
        // check role after save
        if (roleEntityResult == null) {
            System.out.println("can't Create " + roleEntity.getRoleName());
            return;
        }
    }

    @Override
    public void createPrivilege(PrivilegeEntity privilegeEntity) {
        // check duplicate of privilege name
        boolean isPrivilegeName = privilegeRepository.existsByPrivilegeName(privilegeEntity.getPrivilegeName());
        if (isPrivilegeName) {
            System.out.println(privilegeEntity.getPrivilegeName() + " created.");
            return;
        }
        // save
        PrivilegeEntity privilegeResult = privilegeRepository.save(privilegeEntity);
        if (privilegeResult == null) {
            System.out.println("can't create " + privilegeEntity.getPrivilegeName());
            return;
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
        boolean isEmail = accountRepository.existsByEmail(accountEntity.getEmail());
        if (isEmail) {
            System.out.println(accountEntity.getEmail() + " -> created.");
            return;
        }
        // encode password
        accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
        // save
        AccountEntity accountEntityResult = accountRepository.save(accountEntity);
        // check account after save
        if (accountEntityResult == null) {
            System.out.println("can't created " + accountEntity.getEmail());
        }
    }
}
