<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div>

            <div class="form-group">
                <label class="col=sm-2 col-form-label">User name: </label>
                <div class="col-sm-6">
                    <input type="text" name="username" class="form-control" placeholder="User name"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col=sm-2 col-form-label">Password: </label>
                <div class="col-sm-6">
                    <input ype="text" name="password" class="form-control" placeholder="Password"/>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <#if !isRegisterForm>
                <a href="/registration">Add new user</a>
            </#if>

            <button type="submit" class="btn btn-primary">
                <#if isRegisterForm>
                    Create
                <#else>
                    Sign in
                </#if>
            </button>
        </div>
    </form>
</#macro>

<#macro logiout>
    <form action="/logout" method="post">
        <button type="submit" class="btn btn-primary">Sign out</button>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>
</#macro>