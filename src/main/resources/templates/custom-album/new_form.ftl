<#import "../master/master.ftl" as m>
<#import "../spring.ftl" as spring>
<@m.indexmaster page="custom-album/new">
            <form action="/custom-album/save" method="post">
                <@spring.bind path="album" />
                Name:<br>
                <@spring.formInput "album.name" />
                <br>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="Submit">
            </form>
</@m.indexmaster>