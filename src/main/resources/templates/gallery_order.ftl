<#import "master/master.ftl" as m>

<@m.indexmaster page="gallery" title="${albumName}">
=
	      <#if images?? && (images?size > 0)>
		      
				<#assign counter = 1>
				<table style="border:1px;">
				<#list images as image>
			        <tr><td>${image.name}</td><td>${image.id}</td><td><#if image.nextId??>${image.nextId}</#if></td><td><#if image.previousId??>${image.previousId}</#if></td>
			            <td>${image.dateTaken?time}</td>
			          </tr>
			        <#assign counter++>
				</#list>
		  </#if>
</@m.indexmaster>