package com.demo.firstProject.Service.ServiceModel.AccountService;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;

import java.util.List;

public interface InitRegisterModel {
    void createRole(RoleEntity roleEntity);
    RoleEntity getOncRole(String roleName);
    List<RoleEntity> getListRole();
    void createAdmin(AccountEntity accountEntity);
}
