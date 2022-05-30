<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
<div>
    <@l.logiout/>
    <span><a href="/user">User list</a></span>
</div>

<div>
    <form method="post" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Enter your message"/>
        <input type="text" name="tag" placeholder="Enter a tag"/>
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Add</button>
    </form>
</div>
<br>
<div>List of messages:</div>

<form method="get" action="/main">
    <input type="text" name="filter" value="${filter?if_exists}"/>
    <button type="submit">Search</button>
</form>

<#list messages as message>
    <div>
        <b>${message.id}</b>
        <span>${message.text}</span>
        <i>${message.tag}</i>
        <strong>${message.authorName}</strong>
        <div>
<#--            ?? - приведение к boolean type-->
            <#if message.fileName??>
                <img src="/img/${message.fileName}">
            </#if>
        </div>
    </div>
    <#else>
    No messages
</#list>
</@c.page>