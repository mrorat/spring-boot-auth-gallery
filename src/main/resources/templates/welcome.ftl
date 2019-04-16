<#import "master/master.ftl" as m>
<@m.indexmaster>
	Hello <#if currentUser??>${currentUser.username}<#else>User, please <a href="/login-form">login</a></#if> 
	
	<@sec.authorize access="hasRole('ADMIN')">
		<a href="/album/refresh">refresh</a>
	</@sec.authorize>
	<@sec.authorize access="hasRole('ADMIN')">
		Only ADMIN can see this<br>
	</@sec.authorize>
	<@sec.authorize access="hasRole('USER')">
		Only USER can see this<br>
	</@sec.authorize>
	<@sec.authorize access="hasRole('GUEST')">
		Only GUEST can see this<br>
	</@sec.authorize>
</@m.indexmaster>