<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
<g:each in="${ArtificialChemistryUtil.getInstance().getTranslationFunctionReferencedSpecies(it)}" var="variable">
	<g:link controller="acSpecies" action="show" id="${variable.id}">
		${variable.label}
	</g:link>
</g:each>