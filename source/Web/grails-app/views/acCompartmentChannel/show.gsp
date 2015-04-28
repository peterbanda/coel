<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartmentChannel.label', default: 'Compartment Channel')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:ref property="compartment">${it.id + " : " + it.label}</f:ref>

            <hr>

        	<f:display property="id" label="Transformation">
        		<g:render template="displaySourceTargetSpecies" model="['acCompartmentChannelInstance':instance,'direction':instance.direction]" />
        	</f:display>

        	<f:display property="permeability"><g:formatNumber number="${value}" type="number" maxFractionDigits="10"/></f:display>
		</f:with>
    </theme:zone>
</body>
</html>