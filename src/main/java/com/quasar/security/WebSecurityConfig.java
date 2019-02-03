package com.quasar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true
)
@ComponentScan({"com.quasar.*"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected CustomDaoUserDetailsAuthenticationHandler authProvider;
    @Autowired
    private UserDetailsService customUserDetailsService;

    public WebSecurityConfig() {
    }

    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity)((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)
        		((HttpSecurity)http.headers().cacheControl().disable().and()).authorizeRequests().anyRequest()).fullyAuthenticated()
        		.antMatchers(new String[]{"/", "/home"})).permitAll()
        		.antMatchers(new String[]{"/admin/**"})).access("hasRole('ADMIN')")
        		.antMatchers(new String[]{"/images","/user-profile"})).access("hasRole('USER')")
        		.antMatchers(new String[]{"/gallery"})).access("hasRole('GUEST')")
        		.and())
        		.formLogin().and()
        		.logout().logoutUrl("/logout").logoutSuccessUrl("/")
        		.and())
        .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.customUserDetailsService);
        auth.authenticationProvider(this.authProvider);
    }
}
