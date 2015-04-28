<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'singleRunAnalysisSpec.label', default: 'Single Analysis Spec')}" scope="request" />
    <nav:set path="app/chemistry/SingleRunAnalysisSpec"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
		</f:with>
		<hr>
		<g:render template="editablePart"/>
    </theme:zone>
</body>
</html>