<#import "../master/master.ftl" as m>
<@m.indexmaster>
	<script src="../../js/jquery.min.js"></script>
    <script>
	function modifyPermissions(albumId, userId, state){
	        
	        $.ajax({ 
	            url: '/admin/changeAlbumPermissions/'+ albumId + '/' + userId + '/' + state,
	            headers : { '${_csrf.parameterName}' : '${_csrf.token}', 'contentType': 'application/json' },
	            data: { '${_csrf.parameterName}' : '${_csrf.token}' },
	            type: 'POST'
	        }).done(function(responseData) {
	            
	        }).fail(function() {
	            alert('Failed');
	        });
	    } 
    </script>
List of albums !<br>
<table class="standard-table" sty>
   <thead>
    <tr>
      <th>Album name</th>
      <th>Access</th>
      <th>Visible</th>
    </tr>
   </thead>
  <tbody>
<#list albums as ap>
  <tr>
    <td><a href="/album/${ap.album.name}/${ap.album.albumid}">${ap.album.name} <#if ap.album.date??>${ap.album.date?iso_utc}</#if></a></td>
	<td><label class="switch"><input class="matchedit" type="checkbox" <#if ap.albumPermission>checked</#if> 
	onchange="javascript:modifyPermissions('${ap.album.albumid}', '${userId}', <#if ap.albumPermission>true<#else>false</#if>)"><span class="slider round"></span></label></td>
	<td>Visibility</td>
  </tr>
</#list>
</tbody>
</table>
</@m.indexmaster>