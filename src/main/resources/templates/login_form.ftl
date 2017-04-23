<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example </title>
    </head>
    <body>
        <#if Session.SPRING_SECURITY_LAST_EXCEPTION?? && Session.SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
        	<@spring.message "login.bad.credentials"/>
		</#if>		
		<form-login login-page="/login"  method="post"
					username-parameter="j_username" 
					password-parameter="j_password" 
					login-processing-url="/j_spring_security_check" 
					authentication-failure-url="/login?login_error=1">
					<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
		    <label for="username">Username</label><input type="text" id="username" name="j_username"><br/>
		    <label for="password">Password</label><input type="text" id="password" name="j_password"><br/>
		    <input type="submit" value="Login!">
		</form-login>
		<form action="/j_spring_security_check" method="post">
			<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
		    <label for="username">Username</label><input type="text" id="username" name="j_username"><br/>
		    <label for="password">Password</label><input type="text" id="password" name="j_password"><br/>
		    <input type="submit" value="Login!">
		</form>${springMacroRequestContext.getRequestUri()}
    </body>
</html>