<#import "../master/master.ftl" as m>
<@m.indexmaster>
	<script src="../../js/jquery.min.js"></script>
    <script>
	function deleteUser(userId){
	        
	        $.ajax({ 
	            url: '/admin/xxx/'+ userId,
	            headers : { '${_csrf.parameterName}' : '${_csrf.token}', 'Content-Type': 'application/json' },
	            data: { '${_csrf.parameterName}' : '${_csrf.token}' },
	            type: 'DELETE'
	        }).done(function(responseData) {
	            alert('Done: ', responseData);
	        }).fail(function(e) {
	            alert('Failed' + e);
	        });
	    } 
    </script>
    <#include "admin_menu.ftl">
List of users !  <a href="/admin/user<#if withDeleted>">without deleted<#else>?withDeleted=true">with deleted</#if></a><br>
<table class="standard-table" sty>  <thead>
    <tr>
      <th>Username</th>
      <th>Enabled</th>
      <th>Roles</th>
      <th>Albums</th>
      <th>Reset password</th>
      <th>Delete</th>
    </tr>
  </thead>
  <tbody>
<#list users as user>
<tr>
    <td><a href="user/${user.ID}/profile">${user.username}</a></td>
	<td>${user.enabled?c}</td>
	<td><#list user.authorities as auth>${auth}<br/><#else>NONE</#list></td>
    <#if user.deleted?? && !user.deleted>
        <td><a href="/admin/${user.ID}/albumPermissions">modify</a></td>
        <td><a href="/admin/user/${user.ID}/passwordChange">reset password</a></td>
        <td><img onclick="javascript: deleteUser('${user.ID}')" style="width:2em; height:auto;" src="../img/trash_red.png"/></td>
    <#else>
        <td>-</td>
        <td>-</td>
        <td>${user.deletedDate}</td>
    </#if>
</tr>
</#list>
</tbody>
</table>
<sec:csrfMetaTags />
</@m.indexmaster>