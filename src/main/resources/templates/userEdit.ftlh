<#import "parts/common.ftlh" as c>
<@c.page>
    <h5>User editor:</h5>
    <form action="/user" method="post">
        <div>
            <div class="form-group">
                <label class="col=sm-2 col-form-label">User name: </label>
                <div class="col-sm-6">
                    <input type="text" value="${user.username}" name="userName" class="form-control"
                           placeholder="User name">
                </div>
            </div>
            <#list roles as role>
            <#--                <div>-->
            <#--                    <label>-->
            <#--                        <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked","")}>${role}-->
            <#--                    </label>-->
            <#--                </div>-->
                <div class="form-check">
                    <input class="form-check-input" name="${role}" type="checkbox"
                           id="defaultCheck1" ${user.roles?seq_contains(role)?string("checked","")}>
                    <label class="form-check-label" for="defaultCheck1">
                        ${role}
                    </label>
                </div>
            </#list>
            <input type="hidden" value="${user.id}" name="userId">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </form>
</@c.page>