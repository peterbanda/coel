<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoGaSetting.label', default: 'GA Setting')}" scope="request" />
    <nav:set path="app/evolution/GASetting"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>

			<hr>

			<f:display property="name"/>
			<f:display property="eliteNumber"/>
			<f:display property="populationSize"/>
			<f:display property="selectionType"/>

			<f:display property="crossOverType"/>
			<f:display property="crossOverProbability"/>
			<f:display property="conditionalCrossOverFlag"/>

			<f:display property="mutationType"/>
			<f:display property="mutationProbability"/>
			<f:display property="bitMutationType"/>
			<f:display property="perBitMutationProbability"/>
			<f:display property="pertrubMutationStrength"/>
			<f:display property="conditionalMutationFlag"/>

			<f:display property="fitnessRenormalizationType"/>
			<f:display property="generationLimit"/>
			<f:display property="maxValueFlag"/>
		</f:with>
    </theme:zone>
</body>
</html>