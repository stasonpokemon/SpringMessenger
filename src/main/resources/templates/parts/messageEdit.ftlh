<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
   aria-controls="collapseExample">
    Message editor
</a>

<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" name="text" class="form-control ${(textError??)?string('is-invalid','')}"
                       value="<#if message??>${message.text}</#if>" placeholder="Enter your message"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        <#--                            texError - это message в аннотации валидации поля text -->
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="tag" class="form-control ${(tagError??)?string('is-invalid','')}"
                       value="<#if message??>${message.tag}</#if>" placeholder="Enter a tag"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        <#--                            tagError - это message в аннотации валидации поля tag -->
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
            <div class="form-group">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="id" value="<#if message??>${message.id}</#if>">
                <button type="submit" class="btn btn-primary">Save message</button>
            </div>

        </form>
    </div>
</div>