<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluationVariable.label', default: 'Network Evaluation Variable')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
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
        	<f:display property="id"/>
        	<f:display property="variableIndex"/>
			<f:ref controller="networkEvaluation" property="parentSet" textProperty="name"/>
			<hr>
        	<f:display property="label"/>
		</f:with>
	</theme:zone>
</body>
</html>