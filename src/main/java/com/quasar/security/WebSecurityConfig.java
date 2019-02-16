package com.quasar.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
    	RequestMatcher csrfRequestMatcher = new RequestMatcher() {
    	    private Pattern allowedMethods = Pattern.compile("^(GET|DELETE|POST)$");
    	    private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/v[0-9]*/.*", null);

    	    @Override
    	    public boolean matches(HttpServletRequest request) {
    	        // No CSRF due to allowedMethod
    	        if(allowedMethods.matcher(request.getMethod()).matches())
    	            return false;

    	        // No CSRF due to api call
    	        if(apiMatcher.matches(request))
    	            return false;

    	        // CSRF for everything else that is not an API call or an allowedMethod
    	        return true;
    	    }};
    	
        ((HttpSecurity)((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)((AuthorizedUrl)
        		((HttpSecurity)http.headers().cacheControl().disable().and()).csrf().requireCsrfProtectionMatcher(csrfRequestMatcher).and().authorizeRequests().anyRequest()).fullyAuthenticated()
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
