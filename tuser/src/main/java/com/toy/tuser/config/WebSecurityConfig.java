package com.toy.tuser.config;

import com.toy.tuser.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests((authorizeRequests)->
                        authorizeRequests.requestMatchers("/login", "/signup", "/user").permitAll()
                                .anyRequest().authenticated())
                .formLogin((formLogin)->
                        formLogin.loginPage("/login")
                                .defaultSuccessUrl("/articles"))
                .logout((logout)->
                        logout.logoutSuccessUrl("/login")
                                .invalidateHttpSession(true))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth,
                                                       UserDetailsService userDetailsService,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
    }



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}