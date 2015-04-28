   		<ui:field name="acCompartment.subCompartments">
    		<ui:fieldInput>
    			<div class="row-fluid span10">
    			    <div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acCompartment" action="create" params="['parent.id':instance.id]" hint="Add New" text="New"/>
    					<gui:actionLink action="create" onclick="showAddExistingSubCompartmentDialog();return false;" hint="Add Existing" text="Existing"/>
    				</div>
           		    <gui:table domainName="acCompartmentAssociation" list="${instance?.subCompartmentAssociations}">
           		        <gui:column property="order"/>
       					<gui:column property="subCompartment.id"><g:link controller="acCompartment" action="show" id="${it}">${it}</g:link></gui:column>
           		        <gui:column property="subCompartment.label"/>
           		        <gui:column property="subCompartment.channels">
							<ul>
								<g:each in="${it.sort { it.targetSpecies.label }}" var="acSubCompartmentChannelInstance">
                    				<li>
  								        <g:render template="/acCompartmentChannel/displayChannel" bean="${acSubCompartmentChannelInstance}" />
  								    </li>
								</g:each>
							</ul>
            		    </gui:column>
						<gui:column property="subCompartment.channels" label="Permeability">
							<ul>
								<g:each in="${it.sort { it.targetSpecies.label }}" var="acSubCompartmentChannelInstance">
                    				<li>${formatNumber(number: acSubCompartmentChannelInstance.permeability, type: 'number', maxFractionDigits: 4)}</li>
								</g:each>
							</ul>
            		    </gui:column>
            		    <gui:column label="Disassociate?">
							<gui:actionLink action="removeSubCompartmentAssocFromParent" id="${bean.id}" icon="icon-arrow-right"/>
            		    </gui:column>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>