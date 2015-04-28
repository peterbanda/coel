<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSpeciesInteraction.label', default: 'Species Interaction')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
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
			<f:ref property="action.actionSeries" textProperty="name"/>
			<f:ref property="action" textProperty="startTime"/>
            <hr>
            <f:ref property="species" textProperty="label"/>
            <f:display property="settingFunction">${render(template:'displaySettingFunction', bean:instance)}</f:display>
        </f:with>
    </theme:zone>
</body>
</html>