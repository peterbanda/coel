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
				<g:select name="functionType" from="${["Transition Table", "Expression"]}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="function">
			<ui:fieldInput>
				<g:textArea class="input-xxlarge" rows="4" name="functionInput" value="${render(template:'displayFunction',bean:instance.function)}"/>
			</ui:fieldInput>
		</ui:field>
		<ui:field bean="instance" name="multiComponentUpdaterType" required="${true}"/>
		<ui:field bean="instance" name="statesWeightsIntegratorType" required="${false}"/>
    </theme:zone>
</body>
</html>