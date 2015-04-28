<%@ page import="com.banda.math.domain.evo.EvoRun" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcRateConstantTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoNetworkTask" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoRun.label', default: 'Evolution Run')}" scope="request" />
    <nav:set path="app/evolution/EvolutionRun"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>

		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#evoRunChart").chart( {
		           fileNameFun : function() {
					return 'evoRun_' + ${instance.id} + '.csv'
				}					    		
			});

			$("#evoRunChartDrawAll").prop('checked', true);

			$("#evoRunChart").chart("populate", ${chartData});
		});

	</r:script>
</head>
<body>
    <theme:zone name="actions">
		<gui:actionButton action="list"/>
		<gui:actionButton action="create"/>
		<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
		<gui:actionButton action="showPrevious" id="${instance?.id}"/>
		<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

	<theme:zone name="extras">
		<g:render template="dialogs_show"/>
		<g:set var="bestChromosomeInstance" value="${instance?.lastPopulation?.bestOrLastChromosome}" />
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
  			    <g:if test="${instance?.evoTask.getClass() == EvoAcRateConstantTask.class}">  			
    				<li role="presentation"><a role="menuitem" href="${createLink(action: 'importLastBestChromosome', id: instance.id)}">Import Last Best Chromosome</a></li>
    			</g:if>
    			<g:if test="${instance?.evoTask.getClass() == EvoAcInteractionSeriesTask.class}">  			
					<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showImportLastBestAcInteractionSeriesChromosomeDialog();">Import Last Best Chromosome</a></li>
    			</g:if>    			
    			<g:if test="${instance?.evoTask.getClass() == EvoNetworkTask.class}">
    				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showImportLastBestChromosomeDialog();">Import Last Best Chromosome</a></li>
    			</g:if>
				<li role="presentation">
					<g:link action="create" params="['evoTask.id':instance.evoTask.id, 'initChromosomeId':bestChromosomeInstance?.id]">
						Launch New From Last Best Chromosome
					</g:link>
				</li>

				<li role="presentation"><a role="menuitem" href="${createLink(action: 'exportFitnessOnly', id: instance.id)}">Export Fitness Only</a></li>
				<li role="presentation"><a role="menuitem" href="${createLink(action: 'exportWithBestChromosomes', id: instance.id)}">Export Fitness And Best Chromosomes</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
    	<div class="accordion" id="accordion">
			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Specification</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
						<f:with bean="instance">
							<f:display property="id"/>
							<f:display property="timeCreated"/>
							<f:ref property="createdBy" textProperty="username"/>

							<hr>
	
							<f:ref property="evoTask">${it.id +' : ' + it.name}</f:ref>
							<f:ref controller="chromosome" property="initChromosome" textProperty="id"/>
							<f:display property="id" label="Last Best Chromosome">
								<g:link controller="chromosome" action="show" id="${bestChromosomeInstance?.id}">${bestChromosomeInstance?.encodeAsHTML()}</g:link>
							</f:display>
        				</f:with>
					</div>
      			</div>
    		</div>
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">Populations</a>
    			</div>
    			<div id="collapseTwo" class="accordion-body collapse">
      				<div class="accordion-inner">
						<g:render template="populations"/>
      				</div>
      			</div>
    		</div>
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">Fitness Chart</a>
    			</div>
    			<div id="collapseThree" class="accordion-body collapse">
      				<div class="accordion-inner">
      					<div id="evoRunChart" style="float:center">
      				</div>
      			</div>
    		</div>
  		</div>
    </theme:zone>
</body>
</html>