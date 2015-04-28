<g:if test="${logoUri}">
    <a class="brand" href="${g.createLink(uri:'/')}"><r:img uri="${logoUri}" class='theme-logo' alt="${applicationName}"/></a>
</g:if>
<g:else>
    <a class="brand" href="${g.createLink(uri:'/')}">${applicationName.encodeAsHTML()}</a>
</g:else>
