<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#macro indexmaster page="not-named" title="gallery"><html>
<head>
<#if page == "picture">
	<#include "../includes/head_picture.ftl"/>
<#elseif page == "gallery">
	<#include "../includes/head_gallery.ftl"/>
<#elseif page == "album-list">
	<#include "../includes/head_album_list.ftl"/>
<#else>
	<#include "../includes/head.ftl"/>
</#if>
</head>
<body>
<#include "../includes/header.ftl"/>
<#if page?starts_with("admin")>
    <#include "../admin/admin_menu.ftl">
<#else>
    <@sec.authorize access="hasRole('ADMIN')">
        <#if page?starts_with("user/profile")>
            <#include "../admin/admin_menu.ftl">
        </#if>
    </@sec.authorize>
   ${page}
</#if>
<#nested/>
<#include "../includes/footer.ftl"/>
</body>
</html>
</#macro>