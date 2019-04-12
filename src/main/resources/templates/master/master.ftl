<#macro indexmaster page="not-named" title="gallery"><html>
<head>
<#if page == "picture">
	<#include "../includes/head_picture.ftl"/>
<#elseif page == "gallery">
	<#include "../includes/head_gallery.ftl"/>
<#else>
	<#include "../includes/head.ftl"/>
</#if>
</head>
<body>
<#include "../includes/header.ftl"/>
<#nested/>
</body>
<#include "../includes/footer.ftl"/>
</html>
</#macro>