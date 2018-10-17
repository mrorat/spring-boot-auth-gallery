<!DOCTYPE html>
<html>
<head>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="../../css/main.css">
	<link rel="stylesheet" href="../../css/screen.css">
	<script>
		var app = angular.module('myApp', []);
		app.controller('myCtrl', function($scope, $http)
		{
		    $scope.gi = function(albumId, imageId, imgTagId) 
		    {
		    	$http.get('/imagesbase64/' + albumId + '/' + imageId)
		    		.then(function(response)
		    		{
		    			document.getElementById(imgTagId).src = 'data:image/jpeg;base64,' + response.data;
		       		});
		    };
		    $scope.gti = function(albumId, imageId, imgTagId) 
		    {
		    	$http.get('/imagesbase64/thumbnails/' + albumId + '/' + imageId)
		    		.then(function(response)
		    		{
		    			document.getElementById(imgTagId).src = 'data:image/jpeg;base64,' + response.data;
		       		}).catch(function(error) {
                                      console.error(error);
                      });
;
		    };
		});
		$(function() {$(document).keyup(function(e) {
		    alert(e.keyCode);
            switch(e.keyCode)
            {
                case 38 : window.location = $('div.div_back a').attr('href'); break;
            }});
        });
	</script>
</head>
<body>
	<a href="/gallery">Main page</a><div class="div_back"><a href="/gallery" class="back" id="back">back</a></div>
<#if images?? && (images?size > 0)>
	<#assign counter = 1>
	<div ng-app="myApp" ng-controller="myCtrl">
			<div id="main">
				<div class="inner">
					<div class="columns">
		<#assign imagesQty=images?size>
		{{imagesQty}}
</#if>
		<#list images as image>
		    <div class="image fit">
                <a href="{{$location.host()}}/picture/${image.albumId}/${image.id}" data-lightbox="gallery">
                    <img id="image_${counter}" ng-init="gti('${image.albumId}', '${image.id}', 'image_${counter}')" width="23%"></a>
            </div>

          <#assign counter++>
		<#else>
		    There are no pictures in this album. Click !!!<a href="/album/refresh" id="refresh_album">here</a>!!!to refresh.
		</#list>
<#if images?? && (images?size > 0)>
  	</div> <!-- controller -->
  	</div> <!-- columns -->
  	</div> <!-- inner -->
  	</div> <!-- main -->
</#if>
  </body>
</html>