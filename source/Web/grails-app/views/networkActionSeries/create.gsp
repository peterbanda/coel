<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkActionSeries.label', default: 'Network Interaction Series')}" scope="request" />
    <nav:set path="app/network/InteractionSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="periodicity"/>
		<ui:field bean="instance" name="repeatFromElement"/>
		<ui:field bean="instance" name="repetitions"/>
    </theme:zone>
</body>
</html>