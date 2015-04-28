<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'multiRunAnalysisSpec.label', default: 'Multi Analysis Spec')}" scope="request" />
    <nav:set path="app/chemistry/MultiRunAnalysisSpec"/>
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
			<f:ref property="singleRunSpec">${it.id + ' : ' + it.name} </f:ref>
			<f:display property="runNum"/>
			<f:display property="initialStateDistribution">
				<g:render template="/randomDistribution/display" bean="${it}" />
			</f:display>
		</f:with>
    </theme:zone>
</body>
</html>