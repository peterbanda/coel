<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSpecies.label', default: 'Species')}" scope="request" />
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
        	<f:display property="variableIndex"/>
			<f:ref controller="acSpeciesSet" property="parentSet">${it.name + '/' + it.variables.size()}</f:ref>
			<hr>                                        
        	<f:display property="label"/>
        	<f:display property="structure"/>
        </f:with>

		<g:render template="structureImage"/>

    </theme:zone>
</body>
</html>