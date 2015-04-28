    	<ui:field name="population.chromosomes">
    		<ui:fieldInput>
    			<div class="row-fluid span5">
            		<gui:table domainName="chromosome" list="${instance.chromosomes}" showEnabled="true">
        				<gui:column label="Order">${index}</gui:column>
        				<gui:column property="score"/>
        				<gui:column property="fitness"/>
        			</gui:table>
        		</div>
			</ui:fieldInput>
		</ui:field>