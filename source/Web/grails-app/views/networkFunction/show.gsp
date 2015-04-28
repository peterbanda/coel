<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkFunction.label', default: 'Network Function')}" scope="request" />
    <nav:set path="app/network/Function"/>
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
  				<li role="presentation"><a role="menuitem" href="${createLink(action: 'copy', id: instance.id)}"><g:message code="default.copysimple.label"/></a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>

	        <hr>                

			<f:ref property="parentFunction" textProperty="name"/>
			<f:display property="name"/>
			<f:display property="function">${render(template:'displayFunction',bean:value)}</f:display>
			<f:display property="statesWeightsIntegratorType"/>
			<f:display property="multiComponentUpdaterType"/>
		</f:with>

		<g:render template="layerFunctions"/>
    </theme:zone>
</body>
</html>