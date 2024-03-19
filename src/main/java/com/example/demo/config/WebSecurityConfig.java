package com.example.demo.config;

import com.example.demo.providers.AuthProvider;
import com.example.demo.service.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    @Autowired
    MyUserDetails myUserDetails;
    @Bean
    public AuthProvider authProvider(PasswordEncoder passwordEncoder){
        return new AuthProvider(passwordEncoder);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req->req.requestMatchers("/addArticle").authenticated()
                                        .anyRequest().permitAll())
                .formLogin(form->form.loginPage("/login").permitAll());
        http.userDetailsService(myUserDetails);
        //http.authenticationProvider(authProvider);
        System.out.println("securityFilterChain called");
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}