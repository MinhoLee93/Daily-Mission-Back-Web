package com.dailymission.api.springboot.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/", "/api/**", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll();
                //.antMatchers("/api/**").hasRole(Role.USER.name())
                //.anyRequest().authenticated()
                //.and()
                //.cors()
                //.and()
                //.logout()
                //.logoutSuccessUrl("/")
                //.and()
               // .oauth2Login()
                //.userInfoEndpoint();
                //.userService(customOAuth2UserService);
    }
}
