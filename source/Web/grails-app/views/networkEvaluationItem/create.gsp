<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluationItem.label', default: 'Network Evaluation Item')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="evaluation.id" value="${instance.evaluation.id}" />    
		<f:ref bean="instance" property="evaluation" textProperty="name"/>
		<hr>
        <ui:field label="Variable &larr; Function">
        	<ui:fieldInput>
				<g:textField name="variableLabel" value="${instance.variable?.label}" />
				&larr;
				<g:textArea class="input-xxlarge" rows="4" name="evalFunction.formula" value="${render(template:'displayEvaluationFunction', bean:instance)}" />
			</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>