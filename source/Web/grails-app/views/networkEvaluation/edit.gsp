<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluation.label', default: 'Evaluation')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
		</f:with>
        <hr>

        <ui:field bean="instance" name="name"/>
        <ui:field bean="instance" name="evalFunction">
        	<ui:fieldInput>
            	<g:textArea class="input-xxlarge" rows="4" name="evalFunction.formula" value="${render(template:'displayEvaluationFunction', bean: instance)}" />
        	</ui:fieldInput>
        </ui:field>

        <g:render template="evaluationItems"/>
    </theme:zone>
</body>
</html>