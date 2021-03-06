package com.service.backend;

import com.service.backend.Exception.AuthException.AuthExceptionBody;
import com.service.backend.Exception.AuthException.CustomAccessDenied;
import com.service.backend.Exception.AuthException.CustomAuthenticationEntryPoint;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import com.service.backend.JPA.Entity.Account.RoleEntity;
import com.service.backend.Service.Resource.Account.InitRegisterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@EnableCaching
@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/tests/media-type/videos").allowedOrigins("http://localhost:3000");
				registry.addMapping("/api/tests/media-type/images").allowedOrigins("http://localhost:3000");
			}
		};
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
					null, "ADMIN", new ArrayList<>()
			));
			initRegisterService.createRole(new RoleEntity(
					null, "USER", new ArrayList<>()
			));


			// create admin object and access data
			AccountEntity admin = new AccountEntity();
			admin.setUsername("adminProject");
			admin.setPassword("pass123456");
			admin.setRoles(initRegisterService.getListRole());

			// create admin account
			initRegisterService.createAdmin(admin);


			// create user object and access data
			AccountEntity test_user = new AccountEntity();
			test_user.setUsername("userTester");
			test_user.setPassword("pass123456");
			test_user.setRoles(List.of(initRegisterService.getOncRole("USER")));

			// create user account
			initRegisterService.createAdmin(test_user);

		};
	}

}
