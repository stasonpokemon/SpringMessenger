<#import "parts/common.ftlh" as c>

<@c.page>
    <#if isCurrentUser>
        <#include "parts/messageEdit.ftlh">
    </#if>
    <br>
    <#include "parts/messageList.ftlh">
</@c.page>