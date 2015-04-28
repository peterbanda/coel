<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluation.label', default: 'Evaluation')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>

            <hr>

        	<f:display property="name"/>

        	<g:render template="variables"/>
        	<g:render template="evaluationItems"/>

            <f:display property="evalFunction"><g:render template="displayEvaluationFunction" bean="${instance}"/></f:display>
		</f:with>
    </theme:zone>
</body>
</html>