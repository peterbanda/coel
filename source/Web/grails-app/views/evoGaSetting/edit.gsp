<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoGaSetting.label', default: 'GA Setting')}" scope="request" />
    <nav:set path="app/evolution/GASetting"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
		</f:with>

		<hr>

		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="eliteNumber"/>
		<ui:field bean="instance" name="populationSize"/>
		<ui:field bean="instance" name="selectionType" required="${true}"/>

		<ui:field bean="instance" name="crossOverType" required="${true}"/>
		<ui:field bean="instance" name="crossOverProbability"/>
		<ui:field bean="instance" name="conditionalCrossOverFlag"/>

		<ui:field bean="instance" name="mutationType" required="${true}"/>
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