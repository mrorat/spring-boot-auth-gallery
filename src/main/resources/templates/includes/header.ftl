 <div class="header">
  <a href="/gallery" class="logo">Galeria Rodziny Rorat</a>    <#if albumName??><div style="padding-top:0.8em">Album: ${albumName}</div></#if>
  <div class="header-right">
    <a class="active" href="/gallery">Home</a>
    <a href="/logout">Logout</a>
    <sec:authorize ifAllGranted="ROLE_ADMIN">
    	<a href="/admin">Admin</a>
	</sec:authorize>
  </div>
</div>