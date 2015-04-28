<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistry.label', default: 'Artificial Chemistry')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistry"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="generatedBySpec" textProperty="name"/>
		</f:with>

		<hr>

		<ui:field bean="instance" name="name"/>
    	<ui:field bean="instance" name="skinCompartment" from="${acCompartments}" optionValue="${{it.id + ' : ' + it.label}}" required="${true}"/>
    	<ui:field bean="instance" name="simulationConfig" from="${acSimulationConfigs}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>

    	<g:render template="multiRunAnalysisResults"/>
    </theme:zone>
</body>
</html>