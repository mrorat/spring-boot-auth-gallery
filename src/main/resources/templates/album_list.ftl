<#import "master/master.ftl" as m>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>

<@m.indexmaster page="album-list">

List of albums available for user<br>
<input type="text" id="albumNameFilterInput" onkeyup="filter()" placeholder="Search for names..">

<table id="albums">
  <tr class="header">
    <th style="width:60%;">Name</th>
  </tr>
<#list albums as album>
  <tr><td><a href="/album/${album.urlEncodedName?html}/${album.albumid}">${album.name} <#if album.date??>${album.date?iso_utc}</#if></a></td>
      <td><@sec.authorize access="hasRole('ADMIN')"><a href="/admin/album/refresh/${album.albumid}">refresh</a></@sec.authorize></td>
  </tr>
<#else>
    User doesn't have access to any albums
</#list>
</table>
</@m.indexmaster>