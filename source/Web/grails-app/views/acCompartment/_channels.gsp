
		<g:if test="${instance?.channels || !instance.parentCompartments.isEmpty()}">
    		<ui:field name="acCompartment.channels">
    			<ui:fieldInput>
    				<div class="row-fluid span8">
    					<g:if test="${!instance.parentCompartments.isEmpty()}">
    						<div class="spacedTop spacedLeft">
    							<gui:actionLink controller="acCompartmentChannel" action="create" params="['compartment.id':instance.id]" hint="Add New"/>
								<g:if test="${!instance?.channels.isEmpty()}">
									<gui:modal id="confirm-channel-delete-modal" title="Delete" onclick="doTableSelectionAction('acCompartmentChannelTable','deleteMultiple')" text="Are you sure?"/>
    			    				<gui:actionLink icon="icon-trash" onclick="openModal('confirm-channel-delete-modal')" hint="Delete"/>
								</g:if>
    			    		</div>
						</g:if>
            		    <gui:table list="${instance?.channels.sort { it.targetSpecies.label } }" editEnabled="true" checkEnabled="true">
            				<gui:column property="direction"/>
            				<gui:column label="Channel">
								<g:render template="/acCompartmentChannel/displayChannel" bean="${bean}" />
            		    	</gui:column>
            				<gui:column property="permeability">${formatNumber(number: it, type: 'number', maxFractionDigits: 4)}</gui:column>
        				</gui:table>
    			</ui:fieldInput>
			</ui:field>
		</g:if>