	<g:if test="${!instance?.parents.isEmpty()}">
   		<ui:field name="topology.parents">
    		<ui:fieldInput>
				<div class="row-fluid span3">
           		    <gui:table list="${instance?.parents}" showEnabled="true" editEnabled="true">
       					<gui:column property="name"/>
    	        	</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>
    </g:if>