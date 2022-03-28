package com.demo.firstProject.Configuration;

import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Filter.CheckTokenFilter;
import com.demo.firstProject.Service.Resource.Account.JsonwebtokenService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    private AuthenticationEntryPoint authenticationEntryPoint;
    private JsonwebtokenService jsonwebtokenService;

    @Autowired
    private AuthExceptionBody authExceptionBody;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    public ConfigSecurity(AuthenticationEntryPoint authenticationEntryPoint, JsonwebtokenService jsonwebtokenService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jsonwebtokenService = jsonwebtokenService;
    }

    @Override
    public void init(WebSecurity web) throws Exception {
        super.init(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("configure Http");

        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(
    "/api/images/**",
            "/api/animals",
            "/api/accounts/refresh-token",
            "/api/accounts/login",
            "/api/tests/media-type/**",
            "/api/tests/fire-base/**"
        ).permitAll();

        http.authorizeRequests()

        ;


        http.authorizeRequests().antMatchers(
                "/api/tests/streams"
        ).hasAuthority("ADMIN");

        // animal
        http.authorizeRequests()
                // public
                .antMatchers(HttpMethod.GET, "/api/v1/animals").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/animals/{id}").permitAll()
                // request admin
                .antMatchers(HttpMethod.POST, "/api/v1/animals").hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.PUT, "/api/v1/animals/{id}").hasAnyAuthority("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/animals/{id}").hasAnyAuthority("ADMIN", "USER")
        ;

        // animal category
        http.authorizeRequests()
                // public
                .antMatchers(HttpMethod.GET, "/api/animals/v1/categories").permitAll()
                .antMatchers(HttpMethod.GET, "/api/animals/v1/categories/{id}").permitAll()
                // request admin
                .antMatchers(HttpMethod.POST, "/api/animals/v1/categories").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/animals/v1/categories/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/animals/v1/categories/{id}").hasAuthority("ADMIN")
        ;

        http.authorizeRequests().anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http.addFilterBefore(new CheckTokenFilter(jsonwebtokenService, authExceptionBody), BasicAuthenticationFilter.class);

    }



    /*
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
    */


    //    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                        .username("benzyeiei")
//                        .password("rootpassword")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


}
