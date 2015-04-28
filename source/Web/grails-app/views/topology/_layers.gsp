	<g:if test="${!instance?.supportLayers()}">
   		<ui:field name="topology.layers">
    		<ui:fieldInput>
    		    <div class="row-fluid">
    		    	<div class="span4">
                		<div class="pull-right">
							<gui:actionLink controller="topology" action="createTemplate" icon="icon-plus" params="['parent.id':instance.id]">
								<g:message code="topology.addTemplateLayer.label" default="New template layer" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid">
    		    	<div class="span4">
                		<div class="pull-right">
							<gui:actionLink controller="topology" action="createLayered" icon="icon-plus" params="['parent.id':instance.id]">
								<g:message code="topology.addLayeredLayer.label" default="New layered layer" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid">
    		    	<div class="span4">
                		<div class="pull-right">
							<gui:actionLink controller="topology" action="createSpatial" icon="icon-plus" params="['parent.id':instance.id]">
								<g:message code="topology.addSpatialLayer.label" default="New spatial layer" />
							</gui:actionLink>
						</div>
					</div>
				</div>
				<div class="row-fluid">
				    <div class="span4">
						<div class="pull-right">
							<a href="javascript:void(0);" onclick="showAddExistingLayerDialog();"/>
								<i class="icon-plus"></i>
								<g:message code="topology.addExistingLayer.label" default="Existing layer" />
							</a>
						</div>
					</div>
				</div>
				<div class="row-fluid span4">
           		    <gui:table list="${instance?.getLayers()}" showEnabled="true" editEnabled="true">
       					<gui:column property="name"/>
    	        	</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>
    </g:if>