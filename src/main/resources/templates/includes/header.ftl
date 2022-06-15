<div class="header">
  <a href="/gallery" class="logo">Galeria Rodziny Rorat</a>    <#if albumName??><div style="padding-top:0.8em; float: left;">Album: ${albumName}</div></#if>
  <div class="header-right">
    <a <#if springMacroRequestContext.requestUri?contains("/gallery")>class="active"</#if> href="/gallery">Home</a>
    <a href="/logout" title="Logout ${currentUser.username}">Logout</a>
    <a href="/custom-album/new">New custom album</a>
    <@sec.authorize access="hasRole('USER')">
    	<a <#if springMacroRequestContext.requestUri?contains("/user/profile")>class="active"</#if> href="/user/profile">Profile</a>
	</@sec.authorize>
    <@sec.authorize access="hasRole('ADMIN')">
    	<a <#if springMacroRequestContext.requestUri?contains("/admin")>class="active"</#if> href="/admin">Admin</a>
	</@sec.authorize>
  </div>
</div>