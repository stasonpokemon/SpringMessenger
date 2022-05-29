<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    ${message}
    Add new user
    <@l.login "/registration"/>
</@c.page>