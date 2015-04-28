				<div class="row-fluid">
                    <div class="span7">
    			    	<div class="spacedTop spacedLeft">
    		    			<gui:actionLink controller="acInteractionSeries" action="create" params="['parent.id':instance.id, 'speciesSet.id':instance.speciesSet.id]" hint="Add New"/>
    	    		    	<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acInteractionSeriesTable','deleteMultiple');return false;" hint="Delete"/>
        			    </div>
               		    <gui:table list="${instance?.subActionSeries}" showEnabled="true" editEnabled="true" checkEnabled="true">
       					    <gui:column property="name"/>
        			    </gui:table>
                    </div>
            	</div>
