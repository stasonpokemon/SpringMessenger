<#import "parts/common.ftlh" as c>
<@c.page>
    <h3>${userChannel.username}</h3>
    <div>${type}</div>
<#--    <ul class="list-group">-->
    <div class="list-group">
        <label class="list-group-item disabled">Username</label>
        <#list users as user>
        <#--            <li class="list-group-item">-->


            <a href="/user/messages/${user.id}" class="list-group-item list-group-item-action">${user.username}</a>
        <#--            </li>-->
        </#list>
    </div>
<#--    </ul>-->
</@c.page>