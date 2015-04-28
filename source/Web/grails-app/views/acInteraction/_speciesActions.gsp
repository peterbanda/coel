    <r:script>

		$(function(){
			$("#acSpeciesInteractionLink").focus()			
		});

	</r:script>

   		<ui:field name="acInteraction.speciesActions">
    		<ui:fieldInput>
				<div class="row-fluid span8">
    		    	<div class="spacedTop spacedLeft">
    					<gui:actionLink elementId="acSpeciesInteractionLink" tabindex="1" controller="acSpeciesInteraction" action="create" params="['action.id':instance.id]" hint="Add New"/>
    			    	<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acSpeciesInteractionTable','deleteMultiple');return false;" hint="Delete"/>
    			    </div>
           		    <gui:table list="${instance?.speciesActions.sort{ it.species.label } }" checkEnabled="true">
       					<gui:column label="Expression">
       						<g:link controller="acSpeciesInteraction" action="edit" id="${bean.id}">
                       			<g:render template="displaySpeciesAction" bean="${bean}"/>
                       		</g:link>
       					</gui:column>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>