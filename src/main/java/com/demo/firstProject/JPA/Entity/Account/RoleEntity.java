package com.demo.firstProject.JPA.Entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role_tb")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "roles", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccountEntity> listAccountByRole = new ArrayList<>();
}
