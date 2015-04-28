
		<g:if test="${instance?.channels || !instance.parentCompartments.isEmpty()}">
    		<ui:field name="acCompartment.channels">
    			<ui:fieldInput>
    				<div class="row-fluid span8">
    					<g:if test="${!instance.parentCompartments.isEmpty()}">
    						<div class="spacedTop spacedLeft">
    							<gui:actionLink controller="acCompartmentChannel" action="create" params="['compartment.id':instance.id]" hint="Add New"/>
    			    			<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acCompartmentChannelTable','deleteMultiple');return false;" hint="Delete"/>
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