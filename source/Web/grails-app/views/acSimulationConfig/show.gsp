<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSimulationConfig.label', default: 'Simulation Config')}" scope="request" />
    <nav:set path="app/chemistry/SimulationConfig"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="extras">
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
  			    <li role="presentation"><a role="menuitem" href="${createLink(action: 'copy', id: instance.id)}"><g:message code="default.copysimple.label" /></a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>
        	<f:ref property="createdBy" textProperty="username"/>
			<hr>
			<f:display property="name"/>
			<f:display property="odeSolverType"/>
			<f:display property="timeStep">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="tolerance">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="lowerThreshold">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="upperThreshold">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="fixedPointDetectionPrecision">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="fixedPointDetectionPeriodicity">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="influxScale">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
		</f:with>
    </theme:zone>
</body>
</html>