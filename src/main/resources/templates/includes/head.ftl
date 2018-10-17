	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1" />

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
		       		});
		    };
		});
	</script>