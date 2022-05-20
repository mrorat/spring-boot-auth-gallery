<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#import "../master/master.ftl" as m>
<@m.indexmaster page="user/profile">
<@sec.authorize access="hasRole('ADMIN')">
    <#include "../admin/admin_menu.ftl">
</@sec.authorize>
You are logged in as ${currentUser.username}:)
<a href="/user/passwordChangeForm">Change password</a>

<table style="border:1px solid #a2a9b1">
	<tr style="border:1px solid #a2a9b1">
		<th style="border:1px solid #a2a9b1">userid</th>
		<th style="border:1px solid #a2a9b1">username</th>
		<th style="border:1px solid #a2a9b1">is enabled</th>
		<th style="border:1px solid #a2a9b1">is deleted</th>
		<th style="border:1px solid #a2a9b1">createdDate</th>
		<th style="border:1px solid #a2a9b1">deletedDate</th>
	</tr>
	<tr>
		<th style="border:1px solid #a2a9b1">${user.userid}</th>
		<th style="border:1px solid #a2a9b1">${user.username}</th>
		<th style="border:1px solid #a2a9b1"><#if user.enabled??>${user.enabled?c}</#if></th>
		<th style="border:1px solid #a2a9b1"><#if user.deleted??>${user.deleted?c}</#if></th>
		<th style="border:1px solid #a2a9b1">${user.createdDate}</th>
		<th style="border:1px solid #a2a9b1"><#if user.deletedDate??>${user.deletedDate}</#if></th>
	</tr>
</table>	

<#if access_type?? && access_type=="admin">
This user has access to these albums:
<#else>
You have access to these albums:
</#if>
<#list albums as album>
	<li>${album.name} - ${album.albumid}</li><br/>
</#list>
<br>
	
<#if access_type?? && access_type=="admin">
	<#list user.roles as role>
	    Role: ${role}<br/>
	</#list>
<#else>
	<#list currentUser.roles as role>
	    Role: ${role}<br/>
	</#list>
</#if>
</@m.indexmaster>
