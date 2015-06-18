<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkFunction.label', default: 'Network Function')}" scope="request" />
    <nav:set path="app/network/Function"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<g:hiddenField name="parentFunctionId" value="${parentFunctionId}" />
		<ui:field bean="instance" name="name"/>
		<ui:field label="Function Type">
			<ui:fieldInput>
				<g:select name="functionType" from="${["Transition Table", "Expression"]}" disabled="true"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="function">
			<ui:fieldInput>
				${render(template:'displayTransitionTable', bean:instance.function, model: [editable:"true"])}
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="multiComponentUpdaterType" required="${true}"/>
		<ui:field bean="instance" name="statesWeightsIntegratorType" required="${false}"/>
    </theme:zone>
</body>
</html>