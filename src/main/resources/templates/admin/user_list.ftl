<#import "../master/master.ftl" as m>
<@m.indexmaster>
	<script src="../../js/jquery.min.js"></script>
    <script>
	function deleteUser(userId){
	        
	        $.ajax({ 
	            url: '/admin/xxx/'+ userId,
	            headers : { '${_csrf.parameterName}' : '${_csrf.token}', 'content-type': 'application/json' },
	            data: { '${_csrf.parameterName}' : '${_csrf.token}' },
	            type: 'DELETE'
	        }).done(function(responseData) {
	            alert('Done: ', responseData);
	        }).fail(function(e) {
	            alert('Failed' + e);
	        });
	    } 
    </script>
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
    <td><a href="user/profile/${user.ID}">${user.username}</a></td>
	<td>${user.enabled?c}</td>
	<td><#list user.authorities as auth>${auth}<br/><#else>NONE</#list></td>
    <td><a href="/admin/${user.ID}/albumPermissions">modify</a></td>
    <td><a href="/admin/${user.ID}/passwordChange">reset password</a></td>
    <td><#if user.deleted?? && !user.deleted><img onclick="javascript: deleteUser('${user.ID}')" style="width:2em; height:auto;" src="../img/trash_red.png"/><#else></#if></td>
</tr>
</#list>
</tbody>
</table>
<sec:csrfMetaTags />
</@m.indexmaster>