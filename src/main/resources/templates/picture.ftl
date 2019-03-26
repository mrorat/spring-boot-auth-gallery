<#import "master/master.ftl" as m>
<@m.indexmaster><!DOCTYPE html>

	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="../../css/main.css">
	<link rel="stylesheet" href="../../css/screen.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script language="JavaScript" type="text/javascript" src="../../js/jquery-ui-min.js"></script>
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
		});

        var xmlhttp = new XMLHttpRequest();
        var url = "/getImageDescription/${albumId}/${imageId}";
		var enablePrev = false;
		var enableNext = false;
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var imageDescription = JSON.parse(this.responseText);
               
                if (imageDescription.value != null && imageDescription.value.nextId != null 
                	&& imageDescription.value.nextId != imageDescription.value.imageId) {
                    document.getElementById("next").href = "/picture/${albumId}/" + imageDescription.value.nextId;
                    enableNext = true;
                }
                if (imageDescription.value.previousId != null 
                	&& imageDescription.value.previousId != imageDescription.value.imageId)  {
                    document.getElementById("prev").href = "/picture/${albumId}/" + imageDescription.value.previousId;
                    enablePrev = true;
                }
            }
            else {
            	//alert("status of /getImageDescription call: " + this.status);
            }
        };
        xmlhttp.open("GET", url, true);
        xmlhttp.send();

		$(function() {$(document).keyup(function(e) {
            switch(e.keyCode)
            {
                case 37 : if (enablePrev) { window.location = $('div.div_prev a').attr('href'); } break;
                case 38 : window.location = $('div.div_back a').attr('href'); break;
                case 39 : if (enableNext) { window.location = $('div.div_next a').attr('href'); } break;
                case 76 : rotate(-90); break;
                case 80 : rotate(90); break;
                default : alert(e.keyCode); break;
            }});
        });
        
        function rotate(deg) {
        	alert(deg);
        }
	</script>
</head>
<body class="center">
	<div ng-app="myApp" ng-controller="myCtrl" >
        <div style="float: left;">
        	<div class="div_prev"><a href="" class="prev" id="prev">prev</a></div>
	        <div class="div_back"><a href="/album/${albumName}/${albumId}" class="back" id="back">back</a></div>
	        <div class="div_next"><a href="" class="next" id="next">next</a></div>
        </div>
        <img class="image-full-screen" id="image_1" ng-init="gi('${albumId}', '${imageId}', 'image_1')">
  	</div>
</body>

</@m.indexmaster>