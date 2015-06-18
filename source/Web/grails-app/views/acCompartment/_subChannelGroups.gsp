		<g:if test="${instance?.subChannelGroups || !instance.subCompartments.isEmpty()}">
    		<ui:field name="acCompartment.subChannelGroups">
    			<ui:fieldInput>
    				<div class="row-fluid span5">
						<g:if test="${!instance.subCompartments.isEmpty()}">
					  	    <div class="spacedTop spacedLeft">
    							<gui:actionLink controller="acCompartmentChannelGroup" action="create" params="['compartment.id':instance.id]" hint="Add New"/>
								<g:if test="${!instance?.subChannelGroups.isEmpty()}">
									<gui:modal id="confirm-channelGroup-delete-modal" title="Delete" onclick="doTableSelectionAction('acCompartmentChannelGroupTable','deleteMultiple')" text="Are you sure?"/>
									<gui:actionLink icon="icon-trash" onclick="openModal('confirm-channelGroup-delete-modal')" hint="Delete"/>
								</g:if>
    			    		</div>
						</g:if>
            		    <gui:table list="${instance?.subChannelGroups}" showEnabled="true" editEnabled="true" checkEnabled="true">
        					<gui:column property="id"/>
            				<gui:column property="channels">
								<ul>
                    				<g:each in="${it}" var="acCompartmentChannelInstance">
                    					<li>
  								        	<g:render template="/acCompartmentChannel/displayChannel" bean="${acCompartmentChannelInstance}"/>
  								        </li>
								    </g:each>
								</ul>
            				</gui:column>
        				</gui:table>
            		</div>
				</ui:fieldInput>
			</ui:field>
		</g:if>
