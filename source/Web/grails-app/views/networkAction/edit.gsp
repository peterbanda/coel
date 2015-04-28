<%@ page import="com.banda.math.domain.rand.RandomDistributionType" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkAction.label', default: 'Network Interaction')}" scope="request" />
    <nav:set path="app/network/InteractionSeries"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
        <f:display bean="instance" property="id"/>

        <hr>

		<ui:field bean="instance" name="actionSeries" from="${networkActionSeries}" optionValue="${{it.id + ' : ' + it.name}}" />
		<ui:field bean="instance" name="startTime"/>                    
		<ui:field bean="instance" name="timeLength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</ui:field>
		<ui:field bean="instance" name="alternationType"/>
		<ui:field bean="instance" name="stateDistribution">
		<ui:fieldInput>
			<g:select name="stateDistributionType" from="${RandomDistributionType?.values()}"
				keys="${RandomDistributionType?.values()}"
				value="${instance?.stateDistribution?.getType()}"
                noSelection= "['': 'Select One...']"
                onchange="${remoteFunction(
            		action:'updateRandomDistribution',
           			update:'stateDistributionDiv', 
            		params:'\'stateDistributionType=\' + this.value+\'&prefix=state\'' )}"/>
			</ui:fieldInput>
		</ui:field>

		<div id="stateDistributionDiv">
			<g:render template="/randomDistribution/refresh" model="['randomDistribution':instance.stateDistribution,'prefix':'state']" />
		</div>
    </theme:zone>
</body>
</html>