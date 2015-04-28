<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteraction.label', default: 'Interaction')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="actionSeries.id" value="${instance.actionSeries.id}" />
    	<f:ref bean="instance" property="actionSeries" textProperty="name"/>

		<hr>

		<ui:field bean="instance" name="startTime"/>
		<ui:field bean="instance" name="timeLength"><g:formatNumber number="${value}" type="number" maxFractionDigits="10"/></ui:field>
		<ui:field bean="instance" name="alternationType" required="${true}"/>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acInteractionSeries" action="show" id="${instance.actionSeries.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>