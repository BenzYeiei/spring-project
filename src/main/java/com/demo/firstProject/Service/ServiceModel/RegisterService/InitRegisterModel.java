package com.demo.firstProject.Service.ServiceModel.RegisterService;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.PrivilegeEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface InitRegisterModel {
    void createRole(RoleEntity roleEntity);
    void createPrivilege(PrivilegeEntity privilegeEntity);
    RoleEntity getOncRole(String roleName);
    List<RoleEntity> getListRole();
    void createAdmin(AccountEntity accountEntity);
}
