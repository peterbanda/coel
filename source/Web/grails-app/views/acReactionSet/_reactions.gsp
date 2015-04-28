    <r:script>

		$(function(){
			$("#acReactionLink").focus()			
		});

	</r:script>

    			<div class="row-fluid">
    				<div class="spacedTop spacedLeft">
    					<gui:actionLink controller="acReaction" action="create" params="['reactionSet.id':instance.id]" hint="Add New"/>
    			    	<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acReactionTable','deleteMultiple');return false;" hint="Delete"/>
    			    	<gui:actionLink action="copy" onclick="doTableSelectionAction('acReactionTable','copyMultiple');return false;" icon="icon-plus-sign" hint="Copy"/>
    			    </div>
            		<gui:table list="${instance?.reactions}" showEnabled="true" editEnabled="true" checkEnabled="true">
        				<gui:column property="label" editableUrl="/acReaction/updateLabelAjax"/>
        				<gui:column property="group"><g:link controller="acReactionGroup" action="show" id="${it?.id}">${it?.label}</g:link></gui:column>
        				<gui:column label="Reaction" editableUrl="/acReaction/updateReactantsAndProductsAjax"><g:render template="/acReaction/displayReaction" bean="${bean}" /></gui:column>
            			<gui:column label="Forward Rate">
		                	<g:if test="${bean.forwardRateFunction}">
				            	<g:render template="/acReaction/displayForwardRateFunction" bean="${bean}" />
        		  			</g:if>
            				<g:else>
								<g:render template="/acReaction/displayImplicitForwardRateFunction" bean="${bean}" />
							</g:else>
            			</gui:column>
        				<gui:column label="Catalysts" editableUrl="/acReaction/updateCatalystsAjax"><g:render template="/acReaction/displayCatalystsWoLink" bean="${bean}"/></gui:column>
        				<gui:column label="Inhibitors"><g:render template="/acReaction/displayInhibitors" bean="${bean}"/></gui:column>
        			</gui:table>
        		</div>