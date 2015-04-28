   		<ui:field name="acParameterSet.parameters">
    		<ui:fieldInput>
                <div class="row-fluid span4">
					<div class="spacedTop spacedLeft">
						<gui:actionLink controller="acParameter" action="create" params="['parentSet.id':instance.id]" hint="Add New"/>
		    			<gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acParameterTable','deleteMultiple');return false;" hint="Delete"/>
		    		</div>
					<gui:table list="${instance?.variables}" showEnabled="true" editEnabled="true" checkEnabled="true">
       					<gui:column property="label"/>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>