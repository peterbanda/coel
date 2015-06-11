<html>
<head>
    <nav:set path="app/chemistry/RandomRatePerformance"/>
    <r:require module="jquery-ui"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="create" hint="Add New"/>
		<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
       	<filterpane:filterButton/>
 		<filterpane:filterPane domain="${className}"
 			additionalProperties="identifier"
 			associatedProperties="ac.name, compartment.label, actionSeries.name, evaluation.name, simulationConfig.name, actionSeries.id"
			excludeProperties="averagedCorrectRates, timeCreated"
			dialog="true"
			listDistinct="true"
			distinctColumnName="id"/>
	</theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportAsCSVDialog();">Export as CSV</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="compartment"><g:link controller="acCompartment" action="show" id="${it.id}">${it.label}</g:link></gui:column>
            <gui:column property="actionSeries"><g:link controller="acInteractionSeries" action="show" id="${it.id}">${it.name}</g:link></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>