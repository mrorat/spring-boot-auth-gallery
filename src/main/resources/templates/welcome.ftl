<#import "master/master.ftl" as m>
<@m.indexmaster>
	Hello <#if currentUser??>${currentUser.username}<#else>User, please <a href="/login-form">login</a></#if> 
</@m.indexmaster>