<%@ page import="com.banda.function.domain.TransitionTable" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkFunction.label', default: 'Network Function')}" scope="request" />
    <nav:set path="app/network/Function"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
    	<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="parentFunction" textProperty="name"/>
		</f:with>

	    <hr>

		<ui:field bean="instance" name="name"/>
		<g:set var="functionLabel" value="${instance?.function?.getClass() == TransitionTable.class ? 'Transition Table (Function)' : 'Expression (Function)'}" />
		<ui:field bean="instance" name="functionInput" label="${functionLabel}">
			<ui:fieldInput>
                ${render(template:'displayTransitionTable', bean:instance.function, model: [editable:"true"])}
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="multiComponentUpdaterType" required="${true}"/>
		<ui:field bean="instance" name="statesWeightsIntegratorType" required="${false}"/>

		<g:render template="layerFunctions"/>
    </theme:zone>
</body>
</html>