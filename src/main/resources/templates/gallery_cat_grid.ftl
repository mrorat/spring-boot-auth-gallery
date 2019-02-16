<#import "master/master.ftl" as m>
<@m.indexmaster>
 	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../css/reset.css">
    <link rel="stylesheet" href="../../css/styles.css">
    <link rel="stylesheet" href="../../css/main.css">

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
		    $scope.gibg = function(albumId, imageId, sectionTagId) 
		    {
		    	$http.get('/imagesbase64/' + albumId + '/' + imageId)
		    		.then(function(response)
		    		{
		    			var section = document.getElementById(sectionTagId);
		    			section.style.background = "url('data:image/png;base64," + response.data + "')";
		       		}).catch(function(error) {
                        console.error(error);
                    });
		    };
		});

	</script>
  </head>
  <body>
    <main>
      <div ng-app="myApp" ng-controller="myCtrl">
      <section class="cards">
	      <#if images?? && (images?size > 0)>
		      
				<#assign counter = 1>
				<#list images as image>
			        <article>
			        <a href="/picture/${image.albumId}/${image.id}" data-lightbox="gallery">
		                    <img class="article-img<#if image.vertical>-vertical</#if>" id="image_${counter}" 
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
</@m.indexmaster>