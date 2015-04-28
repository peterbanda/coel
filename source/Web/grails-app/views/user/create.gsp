<%@ page import="com.banda.core.domain.um.Role" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'user.label', default: 'User')}" scope="request" />
    <nav:set path="app"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="username"/>
		<ui:field bean="instance" name="password">
			<ui:fieldInput>
				<g:field type="password" name="password" value=""/>
			</ui:fieldInput>
		</ui:field>
		<g:render template="editablePart"/>
		<hr/>
		<ui:field bean="instance" name="accountEnabled"/>
		<ui:field bean="instance" name="accountExpired"/>
		<ui:field bean="instance" name="accountLocked"/>
		<ui:field bean="instance" name="credentialsExpired"/>
		<hr/>
		<ui:field bean="instance" name="roles">
			<ui:fieldInput>
				<g:select name="roles" from="${Role.list()}"
					multiple="yes"
					optionKey="id"
					optionValue="name"
					size="5"
					value="${instance?.roles*.id}" />
			</ui:fieldInput>
		</ui:field>
    </theme:zone>
</body>
</html>