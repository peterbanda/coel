<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'stats.label', default: 'Stats')}" scope="request" />
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>

			<hr>

			<f:display property="pos"/>
			<f:display property="mean"/>
			<f:display property="standardDeviation"/>
			<f:display property="min"/>
			<f:display property="max"/>
		</f:with>
    </theme:zone>
</body>
</html>