    		    <div class="row-fluid">
    		   		<div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acInteraction" action="create" params="['actionSeries.id':instance.id]" hint="Add New"/>
                        <g:if test="${!instance?.actions.isEmpty()}">
						    <gui:modal id="confirm-interaction-delete-modal" title="Delete" onclick="doTableSelectionAction('acInteractionTable','deleteMultiple')" text="Are you sure?"/>
						    <gui:actionLink icon="icon-trash" onclick="openModal('confirm-interaction-delete-modal')" hint="Delete"/>
                        </g:if>
    			    </div>
           		    <gui:table list="${instance?.actions}" showEnabled="true" editEnabled="true" checkEnabled="true">
           		        <gui:column label="Time">
           		    		<g:if test="${bean.timeLength != 0}">
           		        		${bean.startTime + ' - ' + (bean.startTime + bean.timeLength)}
           		        	</g:if>
           		        	<g:else>
           		        		${bean.startTime}
           		        	</g:else>
           		       	</gui:column>
						<gui:column label="Variable/Species Assignment">
							<ul>
                            	<g:each in="${bean.variableAssignments.sort{ it.variable.label }}" var="c">
                                	<li>
                                   		<g:link controller="acInteractionVariableAssignment" action="edit" id="${c.id}">
											<g:render template="/acInteraction/displayVariableAssignment" bean="${c}" />
                                        </g:link>
                                	</li>
                                </g:each>
                                <g:each in="${bean.speciesActions.sort{ it.species.label }}" var="s">
                                    <li>
                                    	<g:link controller="acSpeciesInteraction" action="edit" id="${s.id}">
											<g:render template="/acInteraction/displaySpeciesAction" bean="${s}" />
                                        </g:link>
                                	</li>
                                </g:each>
							</ul>
						</gui:column>
						<gui:column label="Add">
							<g:if test="${instance?.actions.size() == 1 && bean.speciesActions.isEmpty()}">
							    <gui:actionLink controller="acSpeciesInteraction" action="create" params="['action.id':bean.id]" hint="Start here to assign species' initial concentrations" hint-placement="right" hint-show="true">
								    <g:message code="acInteractionSeries.addSpeciesAction.label" default="Species Assignment" />
							    </gui:actionLink>
							</g:if>
                            <g:else>
                                <gui:actionLink controller="acSpeciesInteraction" action="create" params="['action.id':bean.id]">
                                    <g:message code="acInteractionSeries.addSpeciesAction.label" default="Species Assignment" />
                                </gui:actionLink>
                            </g:else>
							</br>
							<gui:actionLink controller="acInteractionVariableAssignment" action="create" params="['action.id':bean.id]">
								<g:message code="acInteractionSeries.addInteractionVariableAssignment.label" default="Variable Assignment" />
							</gui:actionLink>
						</gui:column>
        			</gui:table>
            	</div>