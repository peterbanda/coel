<%@ page import="com.banda.chemistry.domain.AcSymmetricSpecBound" %>
<%@ page import="com.banda.chemistry.domain.AcDNAStrandSpecBound" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistrySpecBound.label', default: 'Artificial Chemistry Spec Bound')}" scope="request" />
    <nav:set path="app/evolution/ArtificialChemistrySpecBound"/>
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

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
			<hr>
			<f:display property="name"/>
			<f:display property="influxRatioFrom"/>
			<f:display property="influxRatioTo"/>
			<f:display property="outfluxRatioFrom"/>
			<f:display property="outfluxRatioTo"/>
			<f:display property="constantSpeciesRatioFrom"/>
			<f:display property="constantSpeciesRatioTo"/>

			<f:display property="rateConstantDistributionLocationFrom"/>
			<f:display property="rateConstantDistributionLocationTo"/>
			<f:display property="rateConstantDistributionShapeFrom"/>
			<f:display property="rateConstantDistributionShapeTo"/>

			<f:display property="influxRateConstantDistributionLocationFrom"/>
			<f:display property="influxRateConstantDistributionLocationTo"/>
			<f:display property="influxRateConstantDistributionShapeFrom"/>
			<f:display property="influxRateConstantDistributionShapeTo"/>

			<f:display property="outfluxRateConstantDistributionLocationFrom"/>
			<f:display property="outfluxRateConstantDistributionLocationTo"/>
			<f:display property="outfluxRateConstantDistributionShapeFrom"/>
			<f:display property="outfluxRateConstantDistributionShapeTo"/>

			<f:display property="outfluxNonReactiveRateConstantDistributionLocationFrom"/>
			<f:display property="outfluxNonReactiveRateConstantDistributionLocationTo"/>
			<f:display property="outfluxNonReactiveRateConstantDistributionShapeFrom"/>
			<f:display property="outfluxNonReactiveRateConstantDistributionShapeTo"/>

			<hr>

            <g:if test="${instance?.getClass() == AcSymmetricSpecBound.class}">

				<f:display property="speciesNumFrom"/>
				<f:display property="speciesNumTo"/>
				<f:display property="reactionNumFrom"/>
				<f:display property="reactionNumTo"/>

				<f:display property="reactantsPerReactionNumberFrom"/>
				<f:display property="reactantsPerReactionNumberTo"/>
				<f:display property="productsPerReactionNumberFrom"/>
				<f:display property="productsPerReactionNumberTo"/>
				<f:display property="catalystsPerReactionNumberFrom"/>
				<f:display property="catalystsPerReactionNumberTo"/>
				<f:display property="inhibitorsPerReactionNumberFrom"/>
				<f:display property="inhibitorsPerReactionNumberTo"/>
			</g:if>

			<g:if test="${instance?.getClass() == AcDNAStrandSpecBound.class}">
				<f:display property="singleStrandsNumFrom"/>
				<f:display property="singleStrandsNumTo"/>
				<f:display property="upperToLowerStrandRatioFrom"/>
				<f:display property="upperToLowerStrandRatioTo"/>
				<f:display property="complementaryStrandsRatioFrom"/>
				<f:display property="complementaryStrandsRatioTo"/>

				<f:display property="upperStrandPartialBindingDistributionLocationFrom"/>
				<f:display property="upperStrandPartialBindingDistributionLocationTo"/>
				<f:display property="upperStrandPartialBindingDistributionShapeFrom"/>
				<f:display property="upperStrandPartialBindingDistributionShapeTo"/>
				<f:display property="useGlobalOrder"/>
			</g:if>
		</f:with>
    </theme:zone>
</body>
</html>