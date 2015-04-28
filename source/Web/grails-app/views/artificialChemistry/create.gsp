<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistry.label', default: 'Artificial Chemistry')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistry"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
    	<ui:field bean="instance" name="name"/>
    	<ui:field bean="instance" name="skinCompartment" from="${acCompartments}" optionValue="${{it.id + ' : ' + it.label}}" required="${true}"/>
    	<ui:field bean="instance" name="simulationConfig" from="${acSimulationConfigs}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
    </theme:zone>
</body>
</html>