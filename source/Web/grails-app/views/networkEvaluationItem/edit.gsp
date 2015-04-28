<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluationItem.label', default: 'Network Evaluation Item')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:ref property="evaluation" textProperty="name"/>
		</f:with>
		<hr>                                        
        <ui:field label="Variable &larr; Function">
        	<ui:fieldInput>
				<g:textField name="variableLabel" value="${instance.variable.label}" />
				&larr;
				<g:textArea class="input-xxlarge" rows="4" name="evalFunction.formula" value="${render(template:'displayEvaluationFunction', bean:instance)}" />
			</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>