    		    <div class="row-fluid">
    		    	<div class="span6">
    		    		<div class="spacedTop spacedLeft">
    						<gui:actionLink controller="acReactionGroup" action="create" params="['reactionSet.id':instance.id]" hint="Add New"/>
    			    		<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acReactionGroupTable','deleteMultiple');return false;" hint="Delete"/>
    			    	</div>
            			<gui:table list="${instance?.groups}" showEnabled="true" editEnabled="true" checkEnabled="true">
	        				<gui:column property="label" editable="true"/>
            				<gui:column label="Num">${bean.reactionsNum}</gui:column>
        				</gui:table>
        			</div>
            	</div>