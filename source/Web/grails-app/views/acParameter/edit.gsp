<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acParameter.label', default: 'Parameter')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
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
        	<f:display property="variableIndex"/>
			<f:ref controller="acParameterSet" property="parentSet">${it.name + '/' + it.variables.size()}</f:ref>
			<hr>                                        
        	<ui:field bean="instance" name="label"/>
        	<ui:field bean="instance" name="evolFunction">
        		<ui:fieldInput>
        			<g:textField name="evolFunction.formula" value="${render(template:'displayEvolFunction', bean:instance)}" />
        		</ui:fieldInput>
        	</ui:field>
        </f:with>
    </theme:zone>
</body>
</html>