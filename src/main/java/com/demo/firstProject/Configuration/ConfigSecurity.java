package com.demo.firstProject.Configuration;

import com.demo.firstProject.Exception.AuthException.AuthExceptionBody;
import com.demo.firstProject.Filter.CheckTokenFilter;
import com.demo.firstProject.Component.JsonwebtokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(
    "/api/images/**",
            "/api/animals",
            "/api/accounts/refresh-token",
            "/api/accounts/login",
            "/api/tests/media-type/**",
            "/api/tests/redis",
            "/api/tests/fire-base/**"
        ).permitAll();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/actuator/**").hasAnyAuthority("ADMIN")
        ;


        http.authorizeRequests().antMatchers(
                "/api/tests/streams"
        ).hasAnyAuthority("ADMIN");

        // animal
        http.authorizeRequests()
                // public
                .antMatchers(HttpMethod.GET, "/api/v1/animals/page").permitAll()
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

}
