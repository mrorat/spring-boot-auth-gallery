<!DOCTYPE html>
<html lang="en-GB">
<head></head>
<body>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	List of albums available for user<br>
<#list albums as album>
    Gallery: ${album}<br/>
<#else>
    User doesn't have access to any albums
</#list>
</body>
</html>