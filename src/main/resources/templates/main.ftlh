<#import "parts/common.ftlh" as c>
<@c.page>

    <#if infoMessage??>
        <div>
            ${infoMessage}
        </div>
    </#if>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?if_exists}"
                       placeholder="Search by tag"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <#include "parts/messageEdit.ftlh">
    <br>
    <#include "parts/messageList.ftlh">

</@c.page>