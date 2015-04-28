    	<ui:field name="acReactionGroup.reactions">
    		<ui:fieldInput>
    			<div class="row-fluid">
            		<gui:table list="${instance?.reactions}" showEnabled="true" editEnabled="true" deleteEnabled="true">
        				<gui:column property="label"/>
        				<gui:column property="group"><g:link controller="acReactionGroup" action="show" id="${it?.id}">${it?.label}</g:link></gui:column>
        				<gui:column label="Reaction"><g:render template="/acReaction/displayReaction" bean="${bean}" /></gui:column>
            			<gui:column label="Forward Rate">
		                	<g:if test="${bean.forwardRateFunction}">
				            	<g:render template="/acReaction/displayForwardRateFunction" bean="${bean}" />
        		  			</g:if>
            				<g:else>
								<g:render template="/acReaction/displayImplicitForwardRateFunction" bean="${bean}" />
							</g:else>
            			</gui:column>
        				<gui:column label="Catalysts"><g:render template="/acReaction/displayCatalysts" bean="${bean}" /></gui:column>
        				<gui:column label="Inhibitors"><g:render template="/acReaction/displayInhibitors" bean="${bean}" /></gui:column>
        			</gui:table>
        		</div>
			</ui:fieldInput>
		</ui:field>