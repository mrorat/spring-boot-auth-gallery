<#import "master/master.ftl" as m>

<@m.indexmaster page="gallery" title="${albumName}">
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
</@m.indexmaster>