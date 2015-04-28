<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoGaSetting.label', default: 'GA Setting')}" scope="request" />
    <nav:set path="app/evolution/GASetting"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="eliteNumber"/>
		<ui:field bean="instance" name="populationSize"/>
		<ui:field bean="instance" name="selectionType"/>

		<ui:field bean="instance" name="crossOverType"/>
		<ui:field bean="instance" name="crossOverProbability"/>
		<ui:field bean="instance" name="conditionalCrossOverFlag"/>

		<ui:field bean="instance" name="mutationType"/>
		<ui:field bean="instance" name="mutationProbability"/>
		<ui:field bean="instance" name="bitMutationType"/>
		<ui:field bean="instance" name="perBitMutationProbability"/>
		<ui:field bean="instance" name="pertrubMutationStrength"/>
		<ui:field bean="instance" name="conditionalMutationFlag"/>

		<ui:field bean="instance" name="fitnessRenormalizationType"/>
		<ui:field bean="instance" name="generationLimit"/>
		<ui:field bean="instance" name="maxValueFlag"/>
    </theme:zone>
</body>
</html>