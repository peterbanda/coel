<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteraction.label', default: 'Interaction')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:ref property="actionSeries" textProperty="name"/>
		</f:with>

        <hr>

		<ui:field bean="instance" name="startTime"/>
		<ui:field bean="instance" name="timeLength"><g:formatNumber number="${value}" type="number" maxFractionDigits="10"/></ui:field>
		<ui:field bean="instance" name="alternationType" required="${true}"/>

		<g:render template="variableAssignments"/>
		<g:render template="speciesActions"/>
    </theme:zone>
</body>
</html>