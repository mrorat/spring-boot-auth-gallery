<!DOCTYPE html>
<html lang="en-GB">
<head>
</head>
<body>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	<a href="/album/refresh">refresh</a>
	<a href="/logout">logout</a>
	List of albums available for user<br>
	Galleries: 
	<ol>
<#list albums as album>
		<li><a href="/album/${album.name}/${album.albumid}">${album.name} <#if album.date??>${album.date?iso_utc}</#if></a><br/>
<#else>
    User doesn't have access to any albums
</#list>
	</ol>
</body>
</html>