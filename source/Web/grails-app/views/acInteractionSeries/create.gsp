<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteractionSeries.label', default: 'Interaction Series')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<g:render template="editablePart"/>
    </theme:zone>
</body>
</html>