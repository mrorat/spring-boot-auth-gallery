<#import "../master/master.ftl" as m>
<@m.indexmaster>
You are logged in as ${currentUser.username}:)
<a href="/user/passwordChangeForm">Change password</a>

You have access to these albums:
	<#list albums as album>
		<li>${album.name} - ${album.albumid}</li><br/>
	</#list>
</@m.indexmaster>
