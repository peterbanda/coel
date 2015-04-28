    <g:if test="${acs}">
    	<ui:field name="artificialChemistrySpec.acs">
    		<ui:fieldInput>
    			<div class="row-fluid span8">
            		<gui:table list="${acs}" showEnabled="true">
        				<gui:column property="id"/>
						<gui:column property="name"/>
            			<gui:column property="createTime"/>
        			</gui:table>
        		</div>
        	</ui:fieldInput>
    	</ui:field>
    </g:if>