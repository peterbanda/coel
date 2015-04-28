<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'network.label', default: 'Network')}" scope="request" />
    <nav:set path="app/network/Network"/>
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
			<f:ref property="topology">${it.id + ' : ' + it.name}</f:ref>
			<f:ref property="function">${it.id + ' : ' + it.name}</f:ref>
			<f:ref property="weightSetting">${it.id + ' : ' + it.name}</f:ref>
		</f:with>
    </theme:zone>
</body>
</html>