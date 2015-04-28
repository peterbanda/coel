<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistry.label', default: 'Artificial Chemistry')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistry"/>
    <theme:layout name="create"/>
</head>
<body>
	<theme:zone name="form">
		<ui:form action="saveQuick">
    		<ui:field bean="instance" name="name"/>
	    	<ui:field bean="instance" name="simulationConfig" from="${acSimulationConfigs}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
       		<ui:actions>
       			<ui:button type="submit" kind="button" mode="primary">${message(code: 'default.button.create.label', default: 'Create')}</ui:button>
       			<ui:button kind="anchor" action="list">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
            </ui:actions>
        </ui:form>
	</theme:zone>
</body>
</html>