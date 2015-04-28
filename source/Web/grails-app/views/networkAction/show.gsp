<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkAction.label', default: 'Network Interaction')}" scope="request" />
    <nav:set path="app/network/InteractionSeries"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>

            <hr>

			<f:ref property="actionSeries">${it.id + ' : ' + it.name}</f:ref>
			<f:display property="startTime"/>                    
			<f:display property="timeLength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="alternationType"/>
			<f:display property="stateDistribution">${render(template:'/randomDistribution/display', bean:value)}</f:display>
		</f:with>
    </theme:zone>
</body>
</html>