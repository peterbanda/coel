<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acParameter.label', default: 'Parameter')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
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
     		<f:display property="variableIndex" label="Index"/>
			<f:ref controller="acParameterSet" property="parentSet">${it.name + '/' + it.variables.size()}</f:ref>
			<hr>
			<f:display property="label"/>
			<f:display property="evolFunction">${render(template:'displayEvolFunction', bean:instance)}</f:display>
		</f:with>
    </theme:zone>
</body>
</html>