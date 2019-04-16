<div class="header">
  <a href="/gallery" class="logo">Galeria Rodziny Rorat</a>    <#if albumName??><div style="padding-top:0.8em; float: left;">Album: ${albumName}</div></#if>
  <div class="header-right">
    <a class="active" href="/gallery">Home</a>
    <a href="/logout" title="Logout ${currentUser.username}">Logout</a>
    <@sec.authorize access="hasRole('USER')">
    	<a href="/user/profile">Profile</a>
	</@sec.authorize>
    <@sec.authorize access="hasRole('ADMIN')">
    	<a href="/admin">Admin</a>
	</@sec.authorize>
  </div>
</div>