<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartmentChannelGroup.label', default: 'Compartment Channel Group')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:ref property="compartment">${it.id + " : " + it.label}</f:ref>
		</f:with>

        <hr>

		<g:render template="channels"/>
    </theme:zone>
</body>
</html>