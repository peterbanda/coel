   		<ui:field name="spatialNeighborhood.neighbors">
    		<ui:fieldInput>
    		    <div class="row-fluid">
    		    	<div class="span4">
                		<div class="pull-right">
							<gui:actionLink controller="spatialNeighbor" action="createTemplate" icon="icon-plus" params="['parent.id':instance.id]">
								<g:message code="spatialNeighborhood.addNeighbor.label" default="New neighbor" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid span4">
           		    <gui:table list="${instance.neighbors}" showEnabled="true" editEnabled="true" deleteEnabled="true">
       					<gui:column property="index"/>
						<gui:column property="coordinateDiffs"/>
    	        	</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>