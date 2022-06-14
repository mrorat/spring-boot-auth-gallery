<#import "../master/master.ftl" as m>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>

<@m.indexmaster page="album-management">
<script src="../../js/jquery.min.js"></script>
<script>
    function deleteAlbum(albumId){
            
            $.ajax({ 
                url: '/admin/deleteAlbum/'+ albumId,
                headers : { '${_csrf.parameterName}' : '${_csrf.token}', 'contentType': 'application/json' },
                data: { '${_csrf.parameterName}' : '${_csrf.token}' },
                type: 'POST'
            }).done(function(responseData) {
                
            }).fail(function() {
                alert('Failed');
            });
        }
</script>
List of albums with their status<br><a href="/admin/albumManagement<#if withDeleted>">without deleted<#else>?withDeleted=true">with deleted</#if></a><br>
<input type="text" id="albumNameFilterInput" onkeyup="filter()" placeholder="Search for names..">

<table id="albums">
  <tr class="header">
    <th style="width:35%;">Name</th>
    <th style="width:10%;">Exists?</th>
    <th style="width:45%;">Path</th>
    <th style="width:10%;">Delete</th>
  </tr>
<#list albums as album>
  <tr><td><a href="/album/${album.name?html}/${album.albumid}">${album.name} <#if album.date??>${album.date?iso_utc}</#if></a></td>
      <td><#if album.doesAlbumDirectoryExists>YES</#if></td>
      <td>${album.path?html}</td>
      <td><#if !album.doesAlbumDirectoryExists><img onclick="javascript: deleteAlbum('${album.albumid}')" style="width:2em; height:auto;" src="../img/trash_red.png"/></#if></td>
  </tr>
<#else>
    User doesn't have access to any albums
</#list>
</table>
</@m.indexmaster>