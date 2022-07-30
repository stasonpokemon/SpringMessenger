<#import "parts/common.ftlh" as c>
<@c.page>
    <h5>${username}</h5>
    ${message?if_exists}

    <form method="post">
        <div>

            <div class="form-group">
                <label class="col=sm-2 col-form-label">Password: </label>
                <div class="col-sm-6">
                    <input type="text" name="password" class="form-control" placeholder="Password"/>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">

            <div class="form-group">
                <label class="col=sm-2 col-form-label">Email</label>
                <div class="col-sm-6">
                    <input type="email" name="email"
                           class="form-control ${(emailError??)?string('is-invalid','')}"
                           placeholder="Email" value="${email!''}"/>
                    <#if emailError??>
                        ${emailError}
                    </#if>
                </div>
            </div>

            <button type="submit" class="btn btn-primary">
                Save
            </button>
        </div>
    </form>

</@c.page>