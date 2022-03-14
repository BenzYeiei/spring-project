package com.demo.firstProject;

import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Exception.AuthException.CustomAccessDenied;
import com.demo.firstProject.Exception.AuthException.CustomAuthenticationEntryPoint;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.PrivilegeEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import com.demo.firstProject.Service.Resource.Account.InitRegisterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FirstProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstProjectApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthExceptionBody authExceptionBody() {
		return new AuthExceptionBody();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(){
		return new CustomAuthenticationEntryPoint(authExceptionBody());
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDenied(authExceptionBody());
	}

	@Bean
	CommandLineRunner runner(InitRegisterService initRegisterService) {
		return args -> {
			// create role
			initRegisterService.createRole(new RoleEntity(
					null, "ADMIN", new ArrayList<>(), new ArrayList<>()
			));
			initRegisterService.createRole(new RoleEntity(
					null, "USER", new ArrayList<>(), new ArrayList<>()
			));

			// create privilege of admin
			initRegisterService.createPrivilege(new PrivilegeEntity(
					null, "ADMIN_WRITE", initRegisterService.getOncRole("ADMIN")
			));
			initRegisterService.createPrivilege(new PrivilegeEntity(
					null, "ADMIN_READ", initRegisterService.getOncRole("ADMIN")
			));
			// create privilege of user
			initRegisterService.createPrivilege(new PrivilegeEntity(
					null, "USER_READ", initRegisterService.getOncRole("USER")
			));

			// create admin account
			initRegisterService.createAdmin(new AccountEntity(
					null, "admin@bmail.com", "pass123456", initRegisterService.getListRole()
			));
			// create simple user
			initRegisterService.createAdmin(new AccountEntity(
					null, "simple_user@bmail.com", "pass123456",
					List.of(initRegisterService.getOncRole("USER"))
			));
		};
	}

}
