<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartmentChannelGroup.label', default: 'Compartment Channel Group')}" scope="request" />
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
		</f:with>

        <hr>

    	<ui:field name="acCompartmentChannelGroup.channels">
    		<ui:fieldInput>
    			<div class="row-fluid span6">
    				<gui:table list="${instance?.channels}" showEnabled="true">
        				<gui:column property="id"/>
            			<gui:column property="direction"/>
            			<gui:column property="sourceSpecies">
            				<g:link controller="acSpecies" action="show" id="${it.id}">${it.label}</g:link>
            			</gui:column>
            			<gui:column property="targetSpecies">
	            			<g:link controller="acSpecies" action="show" id="${it.id}">${it.label}</g:link>
    	        		</gui:column>
        			</gui:table>
        		</div>
        	</ui:fieldInput>
    	</ui:field>
    </theme:zone>
</body>
</html>