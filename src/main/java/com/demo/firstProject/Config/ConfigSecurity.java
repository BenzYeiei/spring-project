package com.demo.firstProject.Config;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Allows restricting access based upon the HttpServletRequest using RequestMatcher implementations
                .authorizeRequests()
                // setting any request
                .anyRequest()
                // any request must authenticated
                .authenticated()
                // add before setting one value
                .and()
                // use basic auth
                .httpBasic()
                // add setting
                .and()
                // use form for login and logout with Customizer.withDefaults()
                .formLogin(Customizer.withDefaults())
        ;
    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                        .username("benzyeiei")
//                        .password("rootpassword")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
