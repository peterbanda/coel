   		<ui:field name="networkFunction.layerFunctions">
    		<ui:fieldInput>
    		    <div class="row-fluid">
    		    	<div class="span8">
                		<div class="pull-right">
							<gui:actionLink controller="networkFunction" action="create" params="['parentFunction.id':instance.id]">
								<g:message code="networkFunction.addSubFunction.label" default="New layer function" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid span8">
           		    <gui:table domainName="networkFunction" list="${instance.layerFunctions}" showEnabled="true" editEnabled="true">
           		        <gui:column property="index"/>
           		        <gui:column property="function">
							<g:render template="displayFunction" bean="${it}" />
            		    </gui:column>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>