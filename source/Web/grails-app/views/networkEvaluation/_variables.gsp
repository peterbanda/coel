   		<ui:field name="networkEvaluation.variables">
    		<ui:fieldInput>
				<div class="row-fluid span4">
					<gui:table id="variables" list="${instance?.variables}" editEnabled="true">
       					<gui:column property="label"/>
        			</gui:table>
            	</div>
        	</ui:fieldInput>
    	</ui:field>