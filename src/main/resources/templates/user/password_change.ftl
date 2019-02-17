<#import "../master/master.ftl" as m>
<#import "../spring.ftl" as spring>
<@m.indexmaster>

            <form action="/user/changePassword" method="post">
                <@spring.bind path="password" />
                New password:<br>
                <@spring.formPasswordInput "password.password" />
                <br>
                Confirm password:<br>
                <@spring.formPasswordInput "password.password2" />
                <br>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="Submit">
            </form>
</@m.indexmaster>