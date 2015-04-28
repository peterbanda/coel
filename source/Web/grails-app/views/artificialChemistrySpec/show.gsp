<%@ page import="com.banda.chemistry.domain.ArtificialChemistrySpec" %>
<%@ page import="com.banda.chemistry.domain.AcSymmetricSpec" %>
<%@ page import="com.banda.chemistry.domain.AcDNAStrandSpec" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistrySpec.label', default: 'Artificial Chemistry Spec')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistrySpec"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="actions">
    	<gui:actionButton action="list"/>
    	<gui:actionButton action="createSymmetricInstance" icon="icon-plus" text="Symmetric Type"/>
    	<gui:actionButton action="createDNAStrandInstance" icon="icon-plus" text="DNA Strand Type"/>
    	<gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
    </theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
  			   	<li role="presentation"><a role="menuitem" href="${createLink(action: 'copy', id: instance.id)}"><g:message code="default.copy.label" args="[domainNameLabel]" /></a></li>
    			<li role="presentation" class="divider"></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showDisplayAnalysisResultsDialog();">Display Analysis Results</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportAnalysisResultsDialog();">Export Analysis Results</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>
        	<f:ref property="createdBy" textProperty="username"/>

	        <hr>

        	<f:display property="name"/>
        	<f:display property="speciesForbiddenRedundancy"/>
        	<f:display property="rateConstantDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
        	<f:display property="influxRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
        	<f:display property="outfluxRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
        	<f:display property="outfluxAll"/>
			<f:display property="influxRateConstantDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
			<f:display property="outfluxRateConstantDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
			<f:display property="outfluxNonReactiveRateConstantDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
			<f:display property="constantSpeciesRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>


	        <hr>
	
            <g:if test="${instance?.getClass() == AcSymmetricSpec.class}">
				<f:display property="speciesNum"/>
				<f:display property="reactionNum"/>
				<f:display property="reactantsPerReactionNumber"/>
				<f:display property="productsPerReactionNumber"/>
				<f:display property="catalystsPerReactionNumber"/>
				<f:display property="inhibitorsPerReactionNumber"/>
			</g:if>

			<g:if test="${instance?.getClass() == AcDNAStrandSpec.class}">
				<f:display property="singleStrandsNum"/>
				<f:display property="upperToLowerStrandRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
				<f:display property="complementaryStrandsRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
				<f:display property="upperStrandPartialBindingDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
				<f:display property="mirrorComplementarity"/>
				<f:display property="useGlobalOrder"/>
			</g:if>
		</f:with>

		<div id="PlotImageDiv"/>

		<g:render template="acs"/>
    </theme:zone>
</body>
</html>