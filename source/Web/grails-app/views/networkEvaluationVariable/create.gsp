<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluationVariable.label', default: 'Network Evaluation Variable')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="parentSet.id" value="${instance.parentSet.id}" />
		<f:ref bean="instance" controller="networkEvaluation" property="parentSet" textProperty="name"/>
		<hr>
        <ui:field bean="instance" name="label"/>
	</theme:zone>
</body>
</html>