<g:if test="${attrs?.remote}">
	<p:callTag tag="g:formRemote" class="${p.joinClasses(values:[formClass,classes])}" attrs="${attrs}">
		${bodyContent}
		${actionsContent}
	</p:callTag>
</g:if>
<g:else>
	<p:callTag tag="g:form" class="${p.joinClasses(values:[formClass,classes])}" attrs="${attrs}">
		${bodyContent}
		${actionsContent}
	</p:callTag>
</g:else>