<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkActionSeries.label', default: 'Network Interaction Series')}" scope="request" />
    <nav:set path="app/network/InteractionSeries"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
		</f:with>

		<hr>

		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="periodicity"/>
		<ui:field bean="instance" name="repeatFromElement"/>
		<ui:field bean="instance" name="repetitions"/>

		<g:render template="actions"/>
    </theme:zone>
</body>
</html>