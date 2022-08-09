<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">
<@c.page>
    <h5>List of users:</h5>

    <table class="table">
        <thead class="thead-light">
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Subscriptions</th>
            <th scope="col">Subscribers</th>
            <#if isAdmin>
                <th scope="col">Role</th>
                <th scope="col">Edit</th>
            </#if>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td><a href="/user/messages/${user.id}">${user.username}</a></td>
                <td><a href="/user/subscriptions/${user.id}/list">subscriptions</a></td>
                <td><a href="/user/subscribers/${user.id}/list">subscribers</a></td>
                <#if isAdmin>
                    <td><#list user.roles as role>${role} <#sep>, </#list></td>
                    <td><a href="/user/${user.id}">edit</a></td>
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>