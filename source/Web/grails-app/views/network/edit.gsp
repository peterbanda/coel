<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'network.label', default: 'Network')}" scope="request" />
    <nav:set path="app/network/Network"/>
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
		<ui:field bean="instance" name="topology" from="${topologies}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
		<ui:field bean="instance" name="function" from="${functions}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
		<ui:field bean="instance" name="weightSetting" from="${weightSettings}" optionValue="${{it.id + ' : ' + it.name}}"/>
    </theme:zone>
</body>
</html>