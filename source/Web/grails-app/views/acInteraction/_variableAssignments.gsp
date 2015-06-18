   		<ui:field name="acInteraction.variableAssignments">
    		<ui:fieldInput>
				<div class="row-fluid span8">
    		    	<div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acInteractionVariableAssignment" action="create" params="['action.id':instance.id]" hint="Add New"/>
						<g:if test="${!instance?.variableAssignments.isEmpty()}">
							<gui:modal id="confirm-variableInteraction-delete-modal" title="Delete" onclick="doTableSelectionAction('acInteractionVariableAssignmentTable','deleteMultiple')" text="Are you sure?"/>
							<gui:actionLink icon="icon-trash" onclick="openModal('confirm-variableInteraction-delete-modal')" hint="Delete"/>
						</g:if>
    			    </div>
           		    <gui:table list="${instance?.variableAssignments.sort{ it.variable.label }}" checkEnabled="true">
       					<gui:column label="Expression">
       						<g:link controller="acInteractionVariableAssignment" action="edit" id="${bean.id}">
                       			<g:render template="displayVariableAssignment" bean="${bean}"/>
                       		</g:link>
       					</gui:column>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>