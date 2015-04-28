<html>
<head>
    <nav:set path="app/evolution/EvolutionRun"/>
    <r:require module="jquery-ui"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="create" hint="Add New"/>
       	<gui:actionButton action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('${domainName}Table','deleteMultiple');return false;" hint="Delete"/>
       	<filterpane:filterButton/>
 		<filterpane:filterPane domain="${className}" associatedProperties="evoTask.name,evoTask.taskType" excludeProperties="timeCreated"/>
	</theme:zone>

	<theme:zone name="extras">
		<g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showShowMultipleDialog();">Show</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="evoTask"><g:link controller="evoTask" action="show" id="${it.id}">${it.id + ' ' + it.name}</g:link></gui:column>
            <gui:column label="Generation">${bean.lastPopulation?.generation}</gui:column>
            <gui:column label="Best Score">
            	<g:set var="chrom" value="${bean.lastPopulation?.bestOrLastChromosome}"/>
            	<g:link controller="chromosome" action="show" id="${chrom?.id}">
                	${formatNumber(number: chrom?.score, type: 'number', maxFractionDigits: 2)}
                </g:link>
            </gui:column>
            <gui:column label="Best Fitness">
            	<g:set var="chrom" value="${bean.lastPopulation?.bestOrLastChromosome}"/>
            	<g:link controller="chromosome" action="show" id="${chrom?.id}">
                	${formatNumber(number: chrom?.fitness, type: 'number', maxFractionDigits: 5)}
                </g:link>
            </gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>