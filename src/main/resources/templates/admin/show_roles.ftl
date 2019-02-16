<#import "../master/master.ftl" as m>
<@m.indexmaster>
List of user roles from advice model!<br>
<#list currentUser.roles as role>
    Role: ${role}<br/>
<#else>
    User doesn't have any roles
</#list>
</@m.indexmaster>