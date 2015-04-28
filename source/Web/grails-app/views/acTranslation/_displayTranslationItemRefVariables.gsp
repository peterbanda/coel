<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
<g:each in="${ArtificialChemistryUtil.getInstance().getTranslationFunctionReferencedVariables(it)}" var="variable">
	<g:link controller="acTranslationVariable" action="show" id="${variable.id}">
		${variable.label}
	</g:link>
</g:each>