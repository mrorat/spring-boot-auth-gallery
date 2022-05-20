<#import "../master/master.ftl" as m>
<@m.indexmaster page="admin/user/album-permissions">
	<script src="../../js/jquery-3.6.0.min.js"></script>
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
function filter(accessType = 'all') {
  // Declare variables
  var input, filter, table, tr, td, i, txtValue, accessTd, input;
  input = document.getElementById("albumNameFilterInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("albums");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
        if (accessType != 'all') {
          accessTd = tr[i].getElementsByTagName("td")[1];
          if (accessType == 'hasAccess' && !accessTd.childNodes[0].childNodes[0].checked) {
            tr[i].style.display = "none";
          } else if (accessType == 'noAccess' && accessTd.childNodes[0].childNodes[0].checked) {
            tr[i].style.display = "none";
          }
        }
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
</script>
<#include "admin_menu.ftl">
<h1>Setting permissions for user: ${userName}</h1>
List of albums !<br>
<form action="/admin/${userId}/albumPermissions">
<input type="text" id="albumNameFilterInput" onkeyup="filter()" placeholder="Search for names..">
  <input type="radio" name="accessType" onChange="filter('all')" value="all" <#if accessType="all">checked="checked"</#if>> all<br>
  <input type="radio" name="accessType" onChange="filter('hasAccess')" value="hasAccess" <#if accessType="hasAccess">checked="checked"</#if>> has access<br>
  <input type="radio" name="accessType" onChange="filter('noAccess')" value="noAccess" <#if accessType="noAccess">checked="checked"</#if>> no access<br>
<table id="albums" class="standard-table" sty>
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