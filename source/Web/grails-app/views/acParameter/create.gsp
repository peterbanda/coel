<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acParameter.label', default: 'Parameter')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="parentSet.id" value="${instance.parentSet.id}" />
       	<f:ref bean="instance" controller="acParameterSet" property="parentSet">${it.name + '/' + it.variables.size()}</f:ref>

		<hr>

    	<ui:field bean="instance" name="label"/>
        <ui:field bean="instance" name="evolFunction">
        	<ui:fieldInput>
        		<g:textField name="evolFunction.formula" value="${render(template:'displayEvolFunction', bean:instance)}" />
        	</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>