<#import "../master/master.ftl" as m>
<#import "../spring.ftl" as spring>
<@m.indexmaster page="admin/user/password-change">
            <form action="/admin/user/${userId}/passwordChange" method="post">
                <@spring.bind path="password" />
                User name: ${userName}<br>
                <br>
                Password:<br>
                <@spring.formInput "password.password" />
     	        <@spring.formInput "password.password2" />
                <br>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="Submit">
            </form>
</@m.indexmaster>