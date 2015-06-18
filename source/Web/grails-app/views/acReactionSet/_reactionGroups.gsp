    		    <div class="row-fluid">
    		    	<div class="span6">
    		    		<div class="spacedTop spacedLeft">
    						<gui:actionLink controller="acReactionGroup" action="create" params="['reactionSet.id':instance.id]" hint="Add New"/>
							<g:if test="${!instance?.groups.isEmpty()}">
								<gui:modal id="confirm-reactionGroup-delete-modal" title="Delete" onclick="doTableSelectionAction('acReactionGroupTable','deleteMultiple')" text="Are you sure?"/>
								<gui:actionLink icon="icon-trash" onclick="openModal('confirm-reactionGroup-delete-modal')" hint="Delete"/>
							</g:if>
    			    	</div>
            			<gui:table list="${instance?.groups}" showEnabled="true" editEnabled="true" checkEnabled="true">
	        				<gui:column property="label" editable="true"/>
            				<gui:column label="Num">${bean.reactionsNum}</gui:column>
        				</gui:table>
        			</div>
            	</div>