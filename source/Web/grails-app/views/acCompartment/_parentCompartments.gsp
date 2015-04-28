    	<g:if test="${instance?.parentCompartments}">
    		<ui:field name="acCompartment.parentCompartments">
    			<ui:fieldInput>
    			    <div class="row-fluid span8">
	    				<gui:table list="${instance?.parentCompartments}" showEnabled="true" editEnabled="true">
    	    				<gui:column property="id"/>
        	    			<gui:column property="label"/>
        				</gui:table>
        			</div>
        		</ui:fieldInput>
    		</ui:field>
    	</g:if>