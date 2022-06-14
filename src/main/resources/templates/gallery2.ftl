<#import "master/master.ftl" as m>
<@m.indexmaster>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	List of albums available for user<br>
	Galleries:
	<ol>
<#list albums as album>
		<li><a href="/album/${album.name}/${album.albumid}">${album.name} <#if album.date??>${album.date?iso_utc}</#if></a></li>
<#else>
    User doesn't have access to any albums
</#list>
	</ol>
</@m.indexmaster>