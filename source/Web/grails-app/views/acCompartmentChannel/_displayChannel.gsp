<%@ page import="com.banda.chemistry.domain.AcChannelDirection"%>
<g:link controller="acCompartmentChannel" action="show" id="${it.id}">
    <g:if test="${it.direction == AcChannelDirection.In}">
    	${it.targetSpecies?.label}
    	&larr;
    	${it.sourceSpecies?.label}
	</g:if>
	<g:else>
		${it.sourceSpecies?.label}
		&rarr;
		${it.targetSpecies?.label}
	</g:else>
</g:link>