<%@ page import="com.banda.math.domain.dynamics.SingleRunAnalysisSpec" %>
<%@ page import="com.banda.math.domain.rand.RandomDistributionType" %>

<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'multiRunAnalysisSpec.label', default: 'Multi Analysis Spec')}" scope="request" />
    <nav:set path="app/chemistry/MultiRunAnalysisSpec"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="singleRunSpec"
			from="${SingleRunAnalysisSpec.list(sort: 'id')}"
			optionValue="${{it.id + ' : ' + it.name}}"
			required="${true}"/>
		<ui:field bean="instance" name="runNum"/>
       	<ui:field bean="instance" name="initialStateDistribution">
       		<ui:fieldInput>
				<g:select name="initialStateDistributionType" from="${RandomDistributionType?.values()}"
					keys="${RandomDistributionType?.values()}"
					value="${multiRunAnalysisSpecInstance?.initialStateDistribution?.getType()}"
					onchange="${remoteFunction(
					action:'updateRandomDistribution',
					update:'initialStateDistributionDiv', 
					params:'\'initialStateDistributionType=\' + this.value+\'&prefix=initialState\'' )}"/>

				<div id="initialStateDistributionDiv">
                 	<g:render template="/randomDistribution/refresh" model="['randomDistribution':multiRunAnalysisSpecInstance.initialStateDistribution,'prefix':'initialState']" />
				</div>
			</ui:fieldInput>
       	</ui:field>
    </theme:zone>
</body>
</html>