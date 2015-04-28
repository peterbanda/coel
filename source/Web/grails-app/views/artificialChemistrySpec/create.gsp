<%@ page import="com.banda.chemistry.domain.ArtificialChemistrySpec" %>
<%@ page import="com.banda.chemistry.domain.AcSymmetricSpec" %>
<%@ page import="com.banda.chemistry.domain.AcDNAStrandSpec" %>
<%@ page import="com.banda.chemistry.domain.AcReactionSpeciesForbiddenRedundancy" %>
<%@ page import="com.banda.math.domain.rand.RandomDistributionType" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistrySpec.label', default: 'Artificial Chemistry Spec')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistrySpec"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="speciesForbiddenRedundancy" from="${AcReactionSpeciesForbiddenRedundancy?.values()}" required="${true}"/>

       	<ui:field bean="instance" name="rateConstantDistribution">
       		<ui:fieldInput>
				<g:select name="rateDistributionType" from="${RandomDistributionType?.values()}"
					keys="${RandomDistributionType?.values()}"
					value="${instance?.rateConstantDistribution?.getType()}"
					noSelection= "['': 'Select One...']"
					onchange="${remoteFunction(
								action:'updateRandomDistribution',
								update:'rateDistributionDiv', 
								params:'\'rateDistributionType=\' + this.value+\'&prefix=rate\'' )}"/>

				<div id="rateDistributionDiv">
                 	<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.rateConstantDistribution,'prefix':'rate']" />
				</div>
			</ui:fieldInput>
       	</ui:field>

		<ui:field bean="instance" name="influxRatio">${formatNumber(number: it, type: 'number', maxFractionDigits: 10)}</ui:field>
		<ui:field bean="instance" name="outfluxRatio">${formatNumber(number: it, type: 'number', maxFractionDigits: 10)}</ui:field>
		<ui:field bean="instance" name="outfluxAll"/>

		<ui:field bean="instance" name="influxRateConstantDistribution">
       		<ui:fieldInput>
				<g:select name="influxRateDistributionType" from="${RandomDistributionType?.values()}"
					keys="${RandomDistributionType?.values()}"
					value="${instance?.influxRateConstantDistribution?.getType()}"
					noSelection= "['': 'Select One...']"
					onchange="${remoteFunction(
								action:'updateRandomDistribution',
								update:'influxRateDistributionDiv', 
								params:'\'influxRateDistributionType=\' + this.value+\'&prefix=influxRate\'' )}"/>

				<div id="influxRateDistributionDiv">
                 	<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.influxRateConstantDistribution,'prefix':'influxRate']" />
				</div>
			</ui:fieldInput>
       	</ui:field>

		<ui:field bean="instance" name="outfluxRateConstantDistribution">
       		<ui:fieldInput>
				<g:select name="outfluxRateDistributionType" from="${RandomDistributionType?.values()}"
					keys="${RandomDistributionType?.values()}"
					value="${instance?.outfluxRateConstantDistribution?.getType()}"
					noSelection= "['': 'Select One...']"
					onchange="${remoteFunction(
								action:'updateRandomDistribution',
								update:'outfluxRateDistributionDiv', 
								params:'\'outfluxRateDistributionType=\' + this.value+\'&prefix=outfluxRate\'' )}"/>

				<div id="outfluxRateDistributionDiv">
                 	<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.outfluxRateConstantDistribution,'prefix':'outfluxRate']" />
				</div>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="outfluxNonReactiveRateConstantDistribution">
       		<ui:fieldInput>
				<g:select name="outfluxNonReactiveRateDistributionType" from="${RandomDistributionType?.values()}"
					keys="${RandomDistributionType?.values()}"
					value="${instance?.outfluxNonReactiveRateConstantDistribution?.getType()}"
					noSelection= "['': 'Select One...']"
					onchange="${remoteFunction(
								action:'updateRandomDistribution',
								update:'outfluxNonReactiveRateDistributionDiv', 
								params:'\'outfluxNonReactiveRateDistributionType=\' + this.value+\'&prefix=outfluxNonReactiveRate\'' )}"/>

				<div id="outfluxNonReactiveRateDistributionDiv">
                 	<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.outfluxNonReactiveRateConstantDistribution,'prefix':'outfluxNonReactiveRate']" />
				</div>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="constantSpeciesRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</ui:field>

		<hr>

		<g:if test="${instance?.getClass() == AcSymmetricSpec.class}">
			<ui:field bean="instance" name="speciesNum"/>
			<ui:field bean="instance" name="reactionNum"/>
			<ui:field bean="instance" name="reactantsPerReactionNumber"/>
			<ui:field bean="instance" name="productsPerReactionNumber"/>
			<ui:field bean="instance" name="catalystsPerReactionNumber"/>
			<ui:field bean="instance" name="inhibitorsPerReactionNumber"/>
		</g:if>

		<g:if test="${instance?.getClass() == AcDNAStrandSpec.class}">
			<ui:field bean="instance" name="singleStrandsNum"/>
			<ui:field bean="instance" name="upperToLowerStrandRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</ui:field>
			<ui:field bean="instance" name="complementaryStrandsRatio">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</ui:field>
			
			<ui:field bean="instance" name="upperStrandPartialBindingDistribution">
       			<ui:fieldInput>
					<g:select name="upperStrandPartialBindingDistributionType" from="${RandomDistributionType?.values()}"
						keys="${RandomDistributionType?.values()}"
						value="${instance?.upperStrandPartialBindingDistribution?.getType()}"
						noSelection= "['': 'Select One...']"
						onchange="${remoteFunction(
								action:'updateRandomDistribution',
								update:'upperStrandPartialBindingDistributionDiv', 
								params:'\'upperStrandPartialBindingDistributionType=\' + this.value+\'&prefix=upperStrandPartialBinding\'' )}"/>

					<div id="upperStrandPartialBindingDistributionDiv">
    	             	<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.upperStrandPartialBindingDistribution,'prefix':'upperStrandPartialBinding']" />
					</div>
				</ui:fieldInput>
			</ui:field>

			<ui:field bean="instance" name="upperStrandPartialBindingDistribution">${render(template:'/randomDistribution/display', bean:value)}</ui:field>
			<ui:field bean="instance" name="mirrorComplementarity"/>
			<ui:field bean="instance" name="useGlobalOrder"/>
		</g:if>
    </theme:zone>
</body>
</html>