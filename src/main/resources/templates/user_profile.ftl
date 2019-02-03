<#import "master/master.ftl" as m>
<@m.indexmaster>
You are logged in as ${currentUser.username}:)

				<#list albums as album>
		<li>${album.name} - ${album.albumid}</li><br/>
				</#list>
</@m.indexmaster>
