<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSimulationConfig.label', default: 'Simulation Config')}" scope="request" />
    <nav:set path="app/chemistry/SimulationConfig"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
    	<ui:field bean="instance" name="odeSolverType" required="${true}"/>
		<ui:field bean="instance" name="timeStep">
			<ui:fieldInput>
				<g:textField name="timeStep" value="${formatNumber(number: instance.timeStep, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="tolerance">
			<ui:fieldInput>
				<g:textField name="tolerance" value="${formatNumber(number: instance.tolerance, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="lowerThreshold">
			<ui:fieldInput>
				<g:textField name="lowerThreshold" value="${formatNumber(number: instance.lowerThreshold, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="upperThreshold">
			<ui:fieldInput>
				<g:textField name="upperThreshold" value="${formatNumber(number: instance.upperThreshold, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="fixedPointDetectionPrecision">
			<ui:fieldInput>
				<g:textField name="fixedPointDetectionPrecision" value="${formatNumber(number: instance.fixedPointDetectionPrecision, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="fixedPointDetectionPeriodicity">
			<ui:fieldInput>
				<g:textField name="fixedPointDetectionPeriodicity" value="${formatNumber(number: instance.fixedPointDetectionPeriodicity, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>

		<ui:field bean="instance" name="influxScale">
			<ui:fieldInput>
				<g:textField name="influxScale" value="${formatNumber(number: instance.influxScale, type: 'number', maxFractionDigits: 10)}"/>
			</ui:fieldInput>
		</ui:field>
    </theme:zone>
</body>
</html>