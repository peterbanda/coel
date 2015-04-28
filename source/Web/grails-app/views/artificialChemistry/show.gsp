<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'artificialChemistry.label', default: 'Artificial Chemistry')}" scope="request" />
    <nav:set path="app/chemistry/ArtificialChemistry"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="extras">
	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showDisplayAnalysisResultsDialog();">Display Analysis Results</a></li>
    			<li role="presentation" class="divider"></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showRunAcForAnalysisResultDialog();">Rerun AC For Analysis</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportRunForAnalysisResultDialog();">Export Run For Analysis</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="generatedBySpec" textProperty="name"/>

	        <hr>

			<f:display property="name"/>
			<f:ref property="skinCompartment">${it.id + ' : ' + it.label}</f:ref>
			<f:ref property="skinCompartment.reactionSet">${it.id + ' : ' + it.label + '/' + it.reactions.size()}</f:ref>
			<f:ref property="simulationConfig">${it.id + ' : ' + it.name}</f:ref>
		</f:with>

		<div id="PlotImageDiv"/>

		<g:render template="multiRunAnalysisResults"/>
    </theme:zone>
</body>
</html>