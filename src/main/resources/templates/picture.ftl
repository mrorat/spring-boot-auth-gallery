<#import "master/master.ftl" as m>
<@m.indexmaster page="picture" title="${albumName}">
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