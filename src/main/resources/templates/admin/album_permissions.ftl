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
function filter() {
  // Declare variables
  var input, filter, table, tr, td, i, txtValue;
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
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
    </script>
<h1>Setting permissions for user: ${userName}</h1>
List of albums !<br>
<input type="text" id="albumNameFilterInput" onkeyup="filter()" placeholder="Search for names..">
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