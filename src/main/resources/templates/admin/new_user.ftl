<#import "../master/master.ftl" as m>
<#import "../spring.ftl" as spring>
<@m.indexmaster>
<#include "admin_menu.ftl"/>
            <form action="/admin/user" method="post">
                <@spring.bind path="user" />
                User name:<br>
                <@spring.formInput "user.name" />
                <br>
                Password:<br>
                <@spring.formInput "user.password" />
                <br>
                <@spring.formSingleSelect "user.role", roles, " " />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="Submit">
            </form>
</@m.indexmaster>