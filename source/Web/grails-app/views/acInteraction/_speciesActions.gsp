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
						<g:if test="${!instance?.speciesActions.isEmpty()}">
                            <gui:modal id="confirm-speciesInteraction-delete-modal" title="Delete" onclick="doTableSelectionAction('acSpeciesInteractionTable','deleteMultiple')" text="Are you sure?"/>
                            <gui:actionLink icon="icon-trash" onclick="openModal('confirm-speciesInteraction-delete-modal')" hint="Delete"/>
						</g:if>
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