    		<ui:field name="networkEvaluation.evaluationItems">
    			<ui:fieldInput>
    			    <div class="row-fluid">
    			   		<div class="span8">
    			   			<div class="pull-right">
							<gui:actionLink controller="networkEvaluationItem" action="create" params="['evaluation.id':instance.id]">
								<g:message code="networkEvaluation.addItem.label" default="Item" />
							</gui:actionLink>
							</div>
						</div>
					</div>
					<div class="row-fluid span8">
            		    <gui:table list="${instance?.evaluationItems}" showEnabled="true" editEnabled="true" deleteEnabled="true">
        					<gui:column label="Variable &larr; Function">
        						${bean.label} &larr; <g:render template="/networkEvaluationItem/displayEvaluationFunction" bean="${bean}" />
        					</gui:column>
        				</gui:table>
            		</div>
    			</ui:fieldInput>
			</ui:field>