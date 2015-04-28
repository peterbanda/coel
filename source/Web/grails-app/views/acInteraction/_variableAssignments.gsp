   		<ui:field name="acInteraction.variableAssignments">
    		<ui:fieldInput>
				<div class="row-fluid span8">
    		    	<div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acInteractionVariableAssignment" action="create" params="['action.id':instance.id]" hint="Add New"/>
    			    	<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acInteractionVariableAssignmentTable','deleteMultiple');return false;" hint="Delete"/>
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