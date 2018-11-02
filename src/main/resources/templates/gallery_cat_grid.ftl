<!DOCTYPE html>
<html>
  <head>
    <title>Gallery - ${albumName}</title>
 	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../css/reset.css">
    <link rel="stylesheet" href="../../css/styles.css">

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
	</script>
  </head>
  <body>
    <header>
      <img class="logo" src="https://emojipedia-us.s3.amazonaws.com/thumbs/120/facebook/111/crystal-ball_1f52e.png" alt="heart" />
      <h1 class="heading">Album: ${albumName}</h1>
    </header>
    <main>
      <section class="leading">
        <p class="leading-bigtext">Hello</p>
        <p class="leading-text">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vitae semper quam. Praesent lobortis tellus quis erat condimentum, a bibendum tortor volutpat.</p>
      </section>
      <div ng-app="myApp" ng-controller="myCtrl">
      <section class="cards">
	      <#if images?? && (images?size > 0)>
		      
				<#assign counter = 1>
				<#list images as image>
			        <article>
			        <a href="/picture/${image.albumId}/${image.id}" data-lightbox="gallery">
		                    <img class="article-img" id="image_${counter}" 
		                    ng-init="gti('${image.albumId}', '${image.id}', 'image_${counter}')" alt=" ">
			          <h1 class="article-title">
			            ${image.dateTaken?time}
			          </h1></a>
			        </article>
			        <#assign counter++>
				</#list>
		  </#if>
      </section>
	  </div>	
    </main>
  </body>
</html>
