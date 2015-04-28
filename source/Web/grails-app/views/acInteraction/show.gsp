<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteraction.label', default: 'Interaction')}" scope="request" />
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
			<f:ref property="actionSeries" textProperty="name"/>

            <hr>

			<f:display property="startTime"/>
			<f:display property="timeLength"><g:formatNumber number="${value}" type="number" maxFractionDigits="5"/></f:display>
			<f:display property="alternationType"/>
		</f:with>

		<g:render template="variableAssignments"/>
		<g:render template="speciesActions"/>
    </theme:zone>
</body>
</html>
