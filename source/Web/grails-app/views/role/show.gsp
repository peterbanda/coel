<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'role.label', default: 'Role')}" scope="request" />
	<nav:set path="app"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<hr>
			<f:display property="name"/>
			<f:display property="description"/>
		</f:with>
    </theme:zone>
</body>
</html>
