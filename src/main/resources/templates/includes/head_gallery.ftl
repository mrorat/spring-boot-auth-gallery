<!-- head_gallery.ftl -->

  	<title>${albumName}</title>
 	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
    <script type="text/javascript" src="../../../js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="../../../static/js/jquery-ui.min.js"></script> 
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

	function renameDialog(newAlbumName){
	        
	        var newAlbumName = prompt("Podaj nowa nazwe albumu:", "${albumName}");
			if (newAlbumName != null && newAlbumName != "" && newAlbumName != "${albumName}") {
		        var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance 
				xmlhttp.open("POST", "/admin/changeAlbumName/${albumId}");
				xmlhttp.setRequestHeader("Content-Type", "application/json");
				xmlhttp.setRequestHeader("${_csrf.parameterName}", "${_csrf.token}");
				var response = xmlhttp.send(JSON.stringify({albumName:newAlbumName}));
				alert(response.status);
			}
	    } 

    
		$(function() {
			$(document).keyup(function(e) {
	            switch(e.keyCode)
	            {
	                case 38 : window.location = "/gallery"; break;
	                case 82 : renameDialog('${albumId}'); break;
	            }
	        });
        });
	</script>
<!-- head_gallery.ftl -->