<g:if test="!${icon}">
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
	<g:if test="${action == 'copy'}">
		<g:set var="icon" value="icon-plus-sign" />
	</g:if>
	<g:if test="${action == 'showPrevious'}">
		<g:set var="icon" value="icon-arrow-left" />
	</g:if>
	<g:if test="${action == 'showNext'}">
		<g:set var="icon" value="icon-arrow-right" />
	</g:if>	
</g:if>

<ui:button kind="anchor" controller="${attrs.controller}" action="${action}" id="${id}" onclick="${attrs.onclick}" params="${attrs.params}" title="${attrs.hint}"><i class="${icon}"></i>${title}</ui:button>