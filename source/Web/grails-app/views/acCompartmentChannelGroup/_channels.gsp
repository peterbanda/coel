    	<ui:field name="acCompartmentChannelGroup.channels">
    		<ui:fieldInput>
    		    <div class="row-fluid span9">
    				<gui:table list="${instance?.compartment.subCompartments*.channels.flatten().sort{ it.sourceSpecies.label } }" editEnabled="true">
            			<gui:column property="direction"/>
            			<gui:column property="sourceSpecies">
            				<g:link controller="acSpecies" action="show" id="${it.id}">${it.label}</g:link>
            			</gui:column>
            			<gui:column property="targetSpecies">
            				<g:link controller="acSpecies" action="show" id="${it.id}">${it.label}</g:link>
            			</gui:column>
						<gui:column property="permeability">${formatNumber(number: it, type: 'number', maxFractionDigits: 4)}</gui:column>
            			<gui:column label="Associated">
							<g:checkBox name="contains${index}" value="${instance.getChannels().contains(bean)}"/>
            			</gui:column>
        			</gui:table>
        		</div>
        	</ui:fieldInput>
    	</ui:field>