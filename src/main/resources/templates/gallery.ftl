<#import "master/master.ftl" as m>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>

<@m.indexmaster>

	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
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

	List of albums available for user<br>
<#list albums as album>
		<li><a href="/album/${album.name?html}/${album.albumid}">${album.name} <#if album.date??>${album.date?iso_utc}</#if></a><br/>
<#else>
    User doesn't have access to any albums
</#list>
	</ol>
</@m.indexmaster>