    <g:if test="${instance.multiRunAnalysisResults}">
    	<ui:field name="artificialChemistry.multiRunAnalysisResults">
    		<ui:fieldInput>
    			<div class="row-fluid span7">
            		<gui:table list="${instance.multiRunAnalysisResults}" showEnabled="true">
        				<gui:column property="id"/>
            			<gui:column property="timeCreated"/>
        			</gui:table>
        		</div>
        	</ui:fieldInput>
    	</ui:field>
    </g:if>