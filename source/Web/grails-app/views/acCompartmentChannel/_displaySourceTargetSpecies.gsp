<%@ page import="com.banda.chemistry.domain.AcChannelDirection"%>
<g:if test="${direction}">
    <g:if test="${direction == AcChannelDirection.In}">
        <g:link controller="acSpecies" action="show" id="${acCompartmentChannelInstance?.targetSpecies?.id}">${acCompartmentChannelInstance?.targetSpecies?.label}</g:link>
    	&larr;
    	<g:link controller="acSpecies" action="show" id="${acCompartmentChannelInstance?.sourceSpecies?.id}">${acCompartmentChannelInstance?.sourceSpecies?.label}</g:link>
	</g:if>
	<g:else>
	    <g:link controller="acSpecies" action="show" id="${acCompartmentChannelInstance?.sourceSpecies?.id}">${acCompartmentChannelInstance?.sourceSpecies?.label}</g:link>
		&rarr;
		<g:link controller="acSpecies" action="show" id="${acCompartmentChannelInstance?.targetSpecies?.id}">${acCompartmentChannelInstance?.targetSpecies?.label}</g:link>
	</g:else>
</g:if>