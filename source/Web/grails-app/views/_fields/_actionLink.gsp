<g:if test="${!icon}">
	<g:if test="${action == 'list'}">
		<g:set var="icon" value="icon-th-list" />
	</g:if>
	<g:if test="${action == 'create'}">
		<g:set var="icon" value="icon-plus" />
	</g:if>
	<g:if test="${action == 'edit'}">
		<g:set var="icon" value="icon-edit" />
	</g:if>
	<g:if test="${action == 'show'}">
		<g:set var="icon" value="icon-eye-open" />
	</g:if>
	<g:if test="${action == 'delete'}">
		<g:set var="icon" value="icon-trash" />
	</g:if>
	<g:if test="${action == 'showPrevious'}">
		<g:set var="icon" value="icon-arrow-left" />
	</g:if>
	<g:if test="${action == 'showNext'}">
		<g:set var="icon" value="icon-arrow-right" />
	</g:if>	
</g:if>
<g:if test="${action == 'delete' && !attrs.onclick}">
	<g:link controller="${attrs.controller}" action="${action}" id="${id}" elementId="${attrs.elementId}" params="${attrs.params}" title="${attrs.hint}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
		<i class="${icon}"></i>
		${title}
	</g:link>
</g:if>
<g:else>
	<g:link controller="${attrs.controller}" action="${action}" id="${id}" elementId="${attrs.elementId}" params="${attrs.params}" onclick="${attrs.onclick}" title="${attrs.hint}">
		<i class="${icon}"></i>
		${title}
	</g:link>
</g:else>