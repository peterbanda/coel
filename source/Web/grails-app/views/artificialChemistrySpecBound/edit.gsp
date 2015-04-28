<%@ page import="com.banda.chemistry.domain.AcSymmetricSpecBound" %>
<%@ page import="com.banda.chemistry.domain.AcDNAStrandSpecBound" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistrySpecBound.label', default: 'Artificial Chemistry Spec Bound')}" scope="request" />
    <nav:set path="app/evolution/ArtificialChemistrySpecBound"/>
    <theme:layout name="edit"/>
</head>
<body>
	<theme:zone name="actions">
    	<gui:actionButton action="list"/>
    	<gui:actionButton action="createSymmetricInstance" icon="icon-plus" text="Symmetric Type"/>
    	<gui:actionButton action="createDNAStrandInstance" icon="icon-plus" text="DNA Strand Type"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>
        	<f:ref property="createdBy" textProperty="username"/>
		</f:with>
	    <hr>

		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="influxRatioFrom"/>
		<ui:field bean="instance" name="influxRatioTo"/>
		<ui:field bean="instance" name="outfluxRatioFrom"/>
		<ui:field bean="instance" name="outfluxRatioTo"/>
		<ui:field bean="instance" name="constantSpeciesRatioFrom"/>
		<ui:field bean="instance" name="constantSpeciesRatioTo"/>

		<ui:field bean="instance" name="rateConstantDistributionLocationFrom"/>
		<ui:field bean="instance" name="rateConstantDistributionLocationTo"/>
		<ui:field bean="instance" name="rateConstantDistributionShapeFrom"/>
		<ui:field bean="instance" name="rateConstantDistributionShapeTo"/>

		<ui:field bean="instance" name="influxRateConstantDistributionLocationFrom"/>
		<ui:field bean="instance" name="influxRateConstantDistributionLocationTo"/>
		<ui:field bean="instance" name="influxRateConstantDistributionShapeFrom"/>
		<ui:field bean="instance" name="influxRateConstantDistributionShapeTo"/>

		<ui:field bean="instance" name="outfluxRateConstantDistributionLocationFrom"/>
		<ui:field bean="instance" name="outfluxRateConstantDistributionLocationTo"/>
		<ui:field bean="instance" name="outfluxRateConstantDistributionShapeFrom"/>
		<ui:field bean="instance" name="outfluxRateConstantDistributionShapeTo"/>

		<ui:field bean="instance" name="outfluxNonReactiveRateConstantDistributionLocationFrom"/>
		<ui:field bean="instance" name="outfluxNonReactiveRateConstantDistributionLocationTo"/>
		<ui:field bean="instance" name="outfluxNonReactiveRateConstantDistributionShapeFrom"/>
		<ui:field bean="instance" name="outfluxNonReactiveRateConstantDistributionShapeTo"/>

		<hr>

		<g:if test="${instance?.getClass() == AcSymmetricSpecBound.class}">
			<ui:field bean="instance" name="speciesNumFrom"/>
			<ui:field bean="instance" name="speciesNumTo"/>
			<ui:field bean="instance" name="reactionNumFrom"/>
			<ui:field bean="instance" name="reactionNumTo"/>

			<ui:field bean="instance" name="reactantsPerReactionNumberFrom"/>
			<ui:field bean="instance" name="reactantsPerReactionNumberTo"/>
			<ui:field bean="instance" name="productsPerReactionNumberFrom"/>
			<ui:field bean="instance" name="productsPerReactionNumberTo"/>
			<ui:field bean="instance" name="catalystsPerReactionNumberFrom"/>
			<ui:field bean="instance" name="catalystsPerReactionNumberTo"/>
			<ui:field bean="instance" name="inhibitorsPerReactionNumberFrom"/>
			<ui:field bean="instance" name="inhibitorsPerReactionNumberTo"/>
		</g:if>

		<g:if test="${instance?.getClass() == AcDNAStrandSpecBound.class}">
			<ui:field bean="instance" name="singleStrandsNumFrom"/>
			<ui:field bean="instance" name="singleStrandsNumTo"/>
			<ui:field bean="instance" name="upperToLowerStrandRatioFrom"/>
			<ui:field bean="instance" name="upperToLowerStrandRatioTo"/>
			<ui:field bean="instance" name="complementaryStrandsRatioFrom"/>
			<ui:field bean="instance" name="complementaryStrandsRatioTo"/>

			<ui:field bean="instance" name="upperStrandPartialBindingDistributionLocationFrom"/>
			<ui:field bean="instance" name="upperStrandPartialBindingDistributionLocationTo"/>
			<ui:field bean="instance" name="upperStrandPartialBindingDistributionShapeFrom"/>
			<ui:field bean="instance" name="upperStrandPartialBindingDistributionShapeTo"/>
			<ui:field bean="instance" name="useGlobalOrder"/>
		</g:if>
    </theme:zone>
</body>
</html>