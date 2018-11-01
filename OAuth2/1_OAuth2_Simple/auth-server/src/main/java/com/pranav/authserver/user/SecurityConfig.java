package com.pranav.authserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * This is the old spring web security configuration.
 * It has no relation to OAuth standards.
 * It is concerned with Authentication rather than Authorization.
 * It also configures which URLs can be accessed by authenticated and un-authenticated users. 
 */
@Configuration
@EnableWebSecurity
@Order(100) //Due to backward compatibility issues, It must be a number greater than 3
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private PasswordEncoder userPasswordEncoder;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userDetailsService)
		.passwordEncoder(userPasswordEncoder);			
	}
	
  	@Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().anonymous();
        //Allow anyone to call get token endpoint        
        http.authorizeRequests().antMatchers("/oauth/token").permitAll()
        .and()
        .authorizeRequests().antMatchers("/oauth/check_token").authenticated()
        .and()
        //Set that Remaining all MUST be authorized
        .authorizeRequests().anyRequest().authenticated();
  	}  	
}
