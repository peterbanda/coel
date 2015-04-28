   		<ui:field name="networkActionSeries.actions">
    		<ui:fieldInput>
    		    <div class="row-fluid">
    		    	<div class="span8">
                		<div class="pull-right">
							<gui:actionLink controller="networkAction" action="create" params="['actionSeries.id':instance.id]">
								<g:message code="networkActionSeries.addAction.label" default="New interaction" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid span8">
           		    <gui:table list="${instance.actions}" showEnabled="true" editEnabled="true" deleteEnabled="true">
           		        <gui:column property="startTime"/>
           		        <gui:column property="timeLength"/>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>