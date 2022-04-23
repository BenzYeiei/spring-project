package com.service.backend.Service.ServiceModel.AccountService;

import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Account.RoleEntity;

import java.util.List;

public interface InitRegisterModel {
    void createRole(RoleEntity roleEntity);
    RoleEntity getOncRole(String roleName);
    List<RoleEntity> getListRole();
    void createAdmin(AccountEntity accountEntity);
}
