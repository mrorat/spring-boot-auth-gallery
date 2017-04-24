<!DOCTYPE html>
<html>
<head>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	<script>
		var app = angular.module('myApp', []);
		app.controller('myCtrl', function($scope, $http)
		{
		    $scope.gi = function(value, imgId) 
		    {
		    	$http.get('http://localhost:8080/imagesbase64/' + value)
		    		.then(function(response)
		    		{
		    			document.getElementById(imgId).src = 'data:image/jpeg;base64,' + response.data;
		       		});
		    };
		});
	</script>
</head>
<body>
	<a href="/gallery">Main page</a>
	<#assign counter = 1>
	<div ng-app="myApp" ng-controller="myCtrl">
	<#list images as image>
  		<img id="image_${counter}" ng-init="gi('${image.id}', 'image_${counter}')">
		<#assign counter++>
	<#else>
	    There are no pictures in this album
	</#list>
	</div>
</body>
</html>