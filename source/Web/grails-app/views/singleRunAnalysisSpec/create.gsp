<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'singleRunAnalysisSpec.label', default: 'Single Analysis Spec')}" scope="request" />
    <nav:set path="app/chemistry/SingleRunAnalysisSpec"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<g:render template="editablePart"/>
    </theme:zone>
</body>
</html>
