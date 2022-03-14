package com.demo.firstProject.JPA.Entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "privilege_tb")
public class PrivilegeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "privilege_name", unique = true, nullable = false)
    private String privilegeName;

    @ManyToOne
    @JoinColumn(name = "role_foreign_key")
    private RoleEntity role = new RoleEntity();

}
