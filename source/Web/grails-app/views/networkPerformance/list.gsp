<html>
<head>
	<nav:set path="app/network/Performance"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="filter">
 		<filterpane:filterPane domain="${className}" associatedProperties="network.name, actionSeries.name, evaluation.name"/>
    </theme:zone>

	<theme:zone name="extras">
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="doTableSelectionAction('networkPerformanceTable','exportAsCSV');">Export as CSV</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="doTableSelectionAction('networkPerformanceTable','showCharts');">Show Charts</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="network"><g:link controller="post" action="show" id="${it.id}">${it.name}</g:link></gui:column>
            <gui:column property="interactionSeries"><g:link controller="networkActionSeries" action="show" id="${it.id}">${it.name}</g:link></gui:column>
            <gui:column property="evaluation"><g:link controller="networkEvaluation" action="show" id="${it.id}">${it.name}</g:link></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>