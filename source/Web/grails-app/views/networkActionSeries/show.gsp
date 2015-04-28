<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkActionSeries.label', default: 'Network Interaction Series')}" scope="request" />
    <nav:set path="app/network/InteractionSeries"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>

			<hr>

			<f:display property="name"/>
			<f:display property="periodicity"/>
			<f:display property="repeatFromElement"/>
			<f:display property="repetitions"/>
		</f:with>

		<g:render template="actions"/>
    </theme:zone>
</body>
</html>
