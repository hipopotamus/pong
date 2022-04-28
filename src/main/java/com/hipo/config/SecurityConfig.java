package com.hipo.config;

import com.hipo.Filter.JwtAuthenticationFilter;
import com.hipo.Filter.JwtAuthorizationFilter;
import com.hipo.Filter.Oauth2SuccessHandler;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.repository.AccountRepository;
import com.hipo.security.CustomAccessDeniedHandler;
import com.hipo.security.CustomAuthenticationEntryPoint;
import com.hipo.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountRepository accountRepository;
    private final JwtProcessor jwtProcessor;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilter(corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProcessor))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), accountRepository, jwtProcessor));

        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/account/**").permitAll()
                .mvcMatchers("/home", "/myLogin").permitAll() //** 홈페이지, 로그인
                .mvcMatchers(HttpMethod.POST, "/account").permitAll() //** 회원가입
                .mvcMatchers("/stomp/**", "/chatting/**").permitAll()
                .mvcMatchers("/pongGame/**").permitAll() // test
                .mvcMatchers("/email/**").permitAll() // email
                .anyRequest().hasAuthority("ROLE_USER");

        http
                .oauth2Login()
                .successHandler(oauth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
