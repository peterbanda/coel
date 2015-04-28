<%@ page import="edu.banda.coel.domain.evo.EvoAcRateConstantTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcSpecTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoNetworkTask" %>
<%@ page import="com.banda.math.domain.StatsType" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoAcTask.label', default: 'Evo Task')}" scope="request" />
    <nav:set path="app/evolution/EvolutionTask"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="list" hint="List"/>
    	<span class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown" hint="Add New">
  				<i class="icon-plus"></i>
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcRateConstantTaskInstance')}">AC Rate Constant Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcInteractionSeriesTaskInstance')}">AC Interaction Series Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcSpecInstance')}">AC Spec Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createNetworkInstance')}">Network Type</a></li>
  			</ul>
		</span>
    	<gui:actionButton action="edit" id="${instance?.id}" hint="Edit"/>
    	<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" hint="Delete"/>
    	<gui:actionButton action="copy" hint="Copy" id="${instance?.id}"/>
    	<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
	    	<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
    		<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
		</g:if>
    </theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showDisplayFitnessDialog();">Display Fitness</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showDisplayFitnessCorrelationDialog();">Display Fitness Correlation</a></li>
				<li role="presentation"><a role="menuitem" href="${createLink(action: 'exportBestChromDensityFitnessRelations', id: instance.id)}">Export Chrom. Density / Fitness</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>

			<hr>

			<f:display property="name"/>
			<f:display property="taskType"/>
			<f:ref property="gaSetting">${it.id + ' : ' + it.name}</f:ref>

			<hr>

			<div id="PlotImageDiv">
            </div>

			<g:if test="${instance?.getClass() == EvoAcRateConstantTask.class}">
				<f:ref property="ac">${it.id + ' : ' + it.name}</f:ref>
				<ui:field bean="instance" name="actionSeries" label="Interaction Series">
					<ui:fieldInput>
						<div class="row-fluid span9">
							<gui:table list="${instance.actionSeries}" showEnabled="true" editEnabled="true">
								<gui:column property="name"/>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>
				<f:ref property="acEvaluation">${it.id + ' : ' + it.name}</f:ref>
				<f:display property="asRepetitions" label="Inter. Series Repetitions"/>
				<f:display property="runSteps" label="Run Time"/>
				<f:display property="lastEvaluationStepsToCount" label="Last Eval. Iterations To Count"/>

				<ui:field bean="instance" name="rateConstantTypeBounds">
					<ui:fieldInput>
						<div class="row-fluid span4">
							<gui:table list="${instance.rateConstantTypeBounds}" showEnabled="true">
       							<gui:column property="rateConstantType"/>
								<gui:column label="From">${bean.bound.from}</gui:column>
								<gui:column label="To">${bean.bound.to}</gui:column>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="fixedRateReactions">
					<ui:fieldInput>
						<div class="row-fluid span3">
							<gui:table list="${instance.fixedRateReactions}" showEnabled="true">
       							<gui:column property="id"/>
								<gui:column property="label"/>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="fixedRateReactionGroups">
					<ui:fieldInput>
						<div class="row-fluid span3">
							<gui:table list="${instance.fixedRateReactionGroups}" showEnabled="true">
       							<gui:column property="id"/>
								<gui:column property="label"/>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>
			</g:if>

			<g:if test="${instance?.getClass() == EvoAcInteractionSeriesTask.class}">
				<f:ref property="ac">${it.id + ' : ' + it.name}</f:ref>
				<ui:field bean="instance" name="actionSeries" label="Interaction Series">
					<ui:fieldInput>
						<div class="row-fluid span9">
							<gui:table list="${instance.actionSeries}" showEnabled="true" editEnabled="true">
								<gui:column property="name"/>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>
				<f:ref property="acEvaluation">${it.id + ' : ' + it.name}</f:ref>
				<f:display property="asRepetitions" label="Inter. Series Repetitions"/>
				<f:display property="runSteps" label="Run Time"/>
				<f:display property="lastEvaluationStepsToCount" label="Last Eval. Iterations To Count"/>

				<ui:field bean="instance" name="speciesAssignmentBounds">
					<ui:fieldInput>
						<div class="row-fluid span7">
							<gui:table list="${instance.speciesAssignmentBounds}">
       							<gui:column property="from"/>
								<gui:column property="to"/>
								<gui:column property="assignments">
									<ul>
										<g:each in="${it}" var="a">
											<li>
												<g:link controller="acSpeciesInteraction" action="show" id="${a.id}">
													<g:render template="/acInteraction/displaySpeciesAction" bean="${a}" />
                                        		</g:link>
                                        	</li>
										</g:each>
									</ul>
								</gui:column>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="variableAssignmentBounds">
					<ui:fieldInput>
						<div class="row-fluid span7">
							<gui:table list="${instance.variableAssignmentBounds}">
       							<gui:column property="from"/>
								<gui:column property="to"/>
								<gui:column property="assignments">
									<ul>
										<g:each in="${it}" var="a">
											<li>
												<g:link controller="acInteractionVariableAssignment" action="show" id="${a.id}">
													<g:render template="/acInteraction/displayVariableAssignment" bean="${a}" />
                                        		</g:link>
                                        	</li>
										</g:each>
									</ul>
								</gui:column>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>
			</g:if>

			<g:if test="${instance?.getClass() == EvoAcSpecTask.class}">
				<f:ref property="acSpecBound" textProperty="name"/>
				<f:ref property="simConfig" textProperty="name"/>
				<f:ref property="multiRunAnalysisSpec" textProperty="name"/>
			</g:if>

			<g:if test="${instance?.getClass() == EvoNetworkTask.class}">
				<f:display property="asRepetitions"/>
				<f:ref property="network">${it.id + ' : ' + it.name}</f:ref>
				<f:display property="runTime"/>
				<f:display property="fixedPointDetectionPeriodicity"/>
				<f:ref property="actionSeries">
					<ul>
						<g:each in="${it}" var="a">
							<li><g:link controller="networkActionSeries" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
						</g:each>
					</ul>
				</f:ref>
				<f:ref property="evaluation">${it.id + ' : ' + it.name}</f:ref>
			</g:if>
		</f:with>
		<hr>
		<g:set var="bestChromosomes" value="${instance?.evolutionRuns*.lastPopulation.bestOrLastChromosome}" />
		<g:set var="bestChromosome" value="${instance.gaSetting.maxValueFlag ? bestChromosomes.max{it.score} : bestChromosomes.min{it.score}}" />
		<g:if test="${bestChromosome}">
			<f:display bean="instance" property="id" label="Last Best Chromosome (Overall)">
				<g:link controller="chromosome" action="show" id="${bestChromosome.id}">${bestChromosome}</g:link>
			</f:display>
		</g:if>
		<g:render template="evoRuns"/>
    </theme:zone>
</body>
</html>