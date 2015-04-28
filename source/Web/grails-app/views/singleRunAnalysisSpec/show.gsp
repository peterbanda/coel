<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'singleRunAnalysisSpec.label', default: 'Single Analysis Spec')}" scope="request" />
    <nav:set path="app/chemistry/SingleRunAnalysisSpec"/>
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
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'copy', id: instance.id)}"><g:message code="default.copysimple.label" args="[domainNameLabel]" /></a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:display property="timeCreated"/>
            <hr>
        	<f:display property="name"/>
        	<f:display property="timeStepLength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="iterations"/>
			<f:display property="lyapunovPerturbationStrength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="derridaPerturbationStrength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="derridaTimeLength">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="derridaResolution">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="timeStepToFilter">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
			<f:display property="fixedPointDetectionPrecision">${formatNumber(number: value, type: 'number', maxFractionDigits: 10)}</f:display>
        </f:with>
    </theme:zone>
</body>
</html>