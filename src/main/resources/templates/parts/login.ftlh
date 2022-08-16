<#include "security.ftlh">
<#macro login path isRegisterForm>

    <form action="${path}" method="post">
        <div>

            <div class="form-group">
                <label class="col=sm-2 col-form-label">User name: </label>
                <div class="col-sm-6">
                    <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                           class="form-control ${(usernameError??)?string('is-invalid','')}"
                           placeholder="User name"/>
                    <#if usernameError??>
                        ${usernameError}
                    </#if>
                </div>
            </div>
            <div class="form-group">
                <label class="col=sm-2 col-form-label">Password: </label>
                <div class="col-sm-6">
                    <input type="text" name="password" class="form-control ${(passwordError??)?string('is-invalid','')}"
                           placeholder="Password"/>
                    <#if passwordError??>
                        ${passwordError}
                    </#if>
                </div>
            </div>

            <#if isRegisterForm>
                <div class="form-group">
                    <label class="col=sm-2 col-form-label">Password confirmation: </label>
                    <div class="col-sm-6">
                        <input type="text" name="password2"
                               class="form-control ${(password2Error??)?string('is-invalid','')}"
                               placeholder="Password confirmation"/>
                        <#if password2Error??>
                            ${password2Error}
                        </#if>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col=sm-2 col-form-label">Email</label>
                    <div class="col-sm-6">
                        <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                               class="form-control ${(emailError??)?string('is-invalid','')}"
                               placeholder="Email"/>
                        <#if emailError??>
                            ${emailError}
                        </#if>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="g-recaptcha" data-sitekey="6LdF0C4hAAAAAM6INob-XQcO4ok00RO1q35R-RI6"></div>
                    <#if captchaError??>
                        <div class="alert alert-danger" role="alert">
                            ${captchaError}
                        </div>
                    </#if>
                </div>
            </#if>

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
        <button type="submit" class="btn btn-primary"><#if user??>Sign out<#else>Sign in</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>
</#macro>

<#--<#macro loginButton>-->
<#--    <form action="/login" method="post">-->
<#--    <button type="submit" class="btn btn-primary">Sign in</button>-->
<#--    <input type="hidden" name="_csrf" value="${_csrf.token}">-->
<#--    </form>-->
<#--</#macro>-->

