<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartmentChannelGroup.label', default: 'Compartment Channel Group')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="compartment.id" value="${instance.compartment.id}" />
        <f:ref bean="instance" property="compartment">${it.id + " : " + it.label}</f:ref>

        <hr>

		<g:render template="channels"/>
    </theme:zone>
</body>
</html>