<#macro login path>
    <form action="${path}" method="post">
        <div>
            <div><label>User name: <input type="text" name="username"/></label></div>
            <div><label>Password: <input type="text" name="password"/></label></div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div><input type="submit" value="Sign in"></div>
        </div>
    </form>
</#macro>

<#macro logiout>
    <form action="/logout" method="post">
        <input type="submit" value="Sign out"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>
</#macro>