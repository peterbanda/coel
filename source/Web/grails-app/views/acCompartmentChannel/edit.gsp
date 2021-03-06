<%@ page import="com.banda.chemistry.domain.AcChannelDirection" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartmentChannel.label', default: 'Compartment Channel')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <theme:layout name="edit"/>
    <r:script>
    	$(function(){
    		updateDirection();
		});
    
    	function updateDirection() {
    		if ($('#direction').val() == 'Out')
    			$('#directionDiv').html('&rarr;');
    		else
    			$('#directionDiv').html('&larr;');
    	}
    </r:script>
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

       <ui:field bean="instance" name="direction">
        	<ui:fieldInput>
        		<g:select name="direction" from="${AcChannelDirection?.values()}" value="${instance.direction}" onchange="updateDirection();"/>
       		</ui:fieldInput>
       	</ui:field>

        <ui:field label="Transformation">
        	<ui:fieldInput>
        		<g:select name="innerSpeciesId" from="${instance.compartment.species}"
        			optionKey="id"
            		optionValue="label"
            		value="${(instance.direction == AcChannelDirection.In) ? instance.targetSpecies?.id : instance.sourceSpecies?.id}"
           			noSelection= "['': 'Select One...']" />
            	<span id="directionDiv">
            		&larr;
            	</span>
				<g:select name="parentSpeciesId" from="${instance.compartment.getParentCompartmentSpecies()}"
        			optionKey="id"
            		optionValue="label"
            		value="${(instance.direction == AcChannelDirection.In) ? instance.sourceSpecies?.id : instance.targetSpecies?.id}"
           			noSelection= "['': 'Select One...']" />
        	</ui:fieldInput>
        </ui:field>

        <ui:field bean="instance" name="permeability">
        	<ui:fieldInput>
        		<g:textField name="permeability" value="${formatNumber(number: instance.permeability, type: 'number', maxFractionDigits: 10)}"/>
       		</ui:fieldInput>
       	</ui:field>
    </theme:zone>
</body>
</html>