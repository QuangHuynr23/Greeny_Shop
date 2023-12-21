package com.webnongsan.greenshop.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // Admin page
        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");

        // If you are not logged in, you will be redirected to the /login page.
        http.authorizeRequests().antMatchers("/checkout").access("hasRole('ROLE_USER')");
        http.authorizeRequests().antMatchers("/addToCart").access("hasRole('ROLE_USER')");

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .defaultSuccessUrl("/?login_success")
                .successHandler(new SuccessHandler()).failureUrl("/login?error=true")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout_success")
                .permitAll();

        // remember-me
        http.rememberMe()
                .rememberMeParameter("remember");

    }

}
