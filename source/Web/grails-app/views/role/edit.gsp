<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'role.label', default: 'Role')}" scope="request" />
	<nav:set path="app"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="description"/>
    </theme:zone>
</body>
</html>