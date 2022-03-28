package com.demo.firstProject;

import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Exception.AuthException.CustomAccessDenied;
import com.demo.firstProject.Exception.AuthException.CustomAuthenticationEntryPoint;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Account.PrivilegeEntity;
import com.demo.firstProject.JPA.Entity.Account.RoleEntity;
import com.demo.firstProject.Service.Resource.Account.InitRegisterService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class FirstProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstProjectApplication.class, args);
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
	public String getBucketName_FireBase() {

		try {
			String bucketName = null;

			File getFile = new File(System.getProperty("user.dir") + "\\secret\\bucket.txt");

			Scanner myReader = new Scanner(getFile);
			while (myReader.hasNextLine()) {
				bucketName = myReader.nextLine();
			}
			myReader.close();
			return bucketName;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}
	}

	@Bean
	public StorageOptions cloud_FireStorage() {

		StorageOptions storageOptions = null;

		try {
			// get admin-sdk
			FileInputStream serviceAccount = serviceAccount = new FileInputStream(
					System.getProperty("user.dir") + "\\secret\\firebase-adminsdk.json"
			);

			Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(
					System.getProperty("user.dir") + "\\secret\\firebase-adminsdk.json"
			));

			return StorageOptions.newBuilder()
					.setProjectId(getBucketName_FireBase().replace(".appspot.com", ""))
//					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setCredentials(credentials)
					.build();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
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
