<#include "security.ftlh">
<div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
            <div>
                <#--            ?? - приведение к boolean type-->
                <#if message.fileName??>
                    <img src="/img/${message.fileName}" class="card-img-top" alt="Card image cap">
                </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user/messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId>
                    <a class="btn btn-secondary" href="/user/messages/${message.author.id}?message=${message.id}">
                        Edit
                    </a>
                </#if>
            </div>
        </div>
    <#else>
        No messages
    </#list>
</div>