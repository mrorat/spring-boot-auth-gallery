package com.quasar.security;

import java.util.Collections;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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

	protected CustomDaoUserDetailsAuthenticationHandler authProvider;
    private UserDetailsService customUserDetailsService;

    @Autowired
    public WebSecurityConfig(CustomDaoUserDetailsAuthenticationHandler authProvider, 
    		UserDetailsService customUserDetailsService) {
    	this.authProvider = authProvider;
    	this.customUserDetailsService = customUserDetailsService;
    }
    
    @Bean
    public RoleHierarchyImpl roleHierarchyImpl() {
    	RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        ((RoleHierarchyImpl) roleHierarchy).setHierarchy("ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_GUEST");
		return roleHierarchy;
    }
    
    @Bean
    public RoleHierarchyVoter roleHierarchyVoter() {
    	return new RoleHierarchyVoter(roleHierarchyImpl());
    }
    
    @Bean
    public AffirmativeBased affirmativeBased() {
    	return new AffirmativeBased(Collections.singletonList(roleHierarchyVoter()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	RequestMatcher csrfRequestMatcher = new RequestMatcher() {
    	    private Pattern allowedMethods = Pattern.compile("^(GET|DELETE|POST)$");
    	    private RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/v[0-9]*/.*", null);

    	    @Override
    	    public boolean matches(HttpServletRequest request) {
    	        // No CSRF due to allowedMethod
    	        if(allowedMethods.matcher(request.getMethod()).matches())
    	            return false;

    	        // No CSRF due to API call
    	        if(apiMatcher.matches(request))
    	            return false;

    	        // CSRF for everything else that is not an API call or an allowedMethod
    	        return true;
    	    }};
    	
        	http.headers().cacheControl().disable().and()
        		.csrf().requireCsrfProtectionMatcher(csrfRequestMatcher).and()
        		.authorizeRequests().anyRequest().fullyAuthenticated()
        		.antMatchers(new String[]{"/", "/home"}).anonymous()
        		.antMatchers(new String[]{"/admin/**"}).hasRole("ADMIN")
        		.antMatchers(new String[]{"/profile/**"}).hasRole("USER")
        		.antMatchers(new String[]{"/images","/user-profile"}).hasRole("USER")
        		.antMatchers(new String[]{"/gallery"}).hasRole("GUEST").and()
        		.formLogin().and()
        		.logout().logoutUrl("/logout").logoutSuccessUrl("/").and()
        		.exceptionHandling().accessDeniedPage("/access-denied");
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
            DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
            expressionHandler.setRoleHierarchy(this.roleHierarchyImpl());
            web.expressionHandler(expressionHandler);
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