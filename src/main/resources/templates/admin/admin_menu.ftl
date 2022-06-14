<div class="topnav" id="myTopnav">
    <a href="/admin" <#if page?starts_with("admin/administration")>class="active"</#if>>Home</a>
    <a href="/admin/user" <#if page?starts_with("admin/user/list")>class="active"</#if>>User list</a>
    <a href="/admin/show-roles" <#if page?starts_with("admin/user/roles")>class="active"</#if>>User's roles</a>
    <a href="/admin/add-user" <#if page == "admin/user/new">class="active"</#if>>Add user</a>
    <a href="/admin/album/refresh" <#if page="refresh">class="active"</#if>>Refresh</a>
    <a href="javascript:void(0);" class="icon" onclick="myFunction()"><i class="fa fa-bars"></i></a>
</div>