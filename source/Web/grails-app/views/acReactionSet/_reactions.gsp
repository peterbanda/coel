    <r:script>

        $(function () {
            $("#acReactionLink").focus()
        });

        function checkIfSpeciesExist() {
            if ($("#speciesLabels").find("[id^='label']").length == 0) {
                showErrorMessage("No species defined.")
                $("[hint-show=true]").popover('show');
                return false;
            }
            return true;
        }
    </r:script>

    			<div class="row-fluid">
    				<div class="spacedTop spacedLeft">
						<g:if test="${!instance?.reactions.isEmpty()}">
							<gui:actionLink controller="acReaction" action="create" elementId="acReactionLink" params="['reactionSet.id':instance.id]" onclick="return checkIfSpeciesExist();" hint="Add New"/>
							<gui:modal id="confirm-reaction-delete-modal" title="Delete" onclick="doTableSelectionAction('acReactionTable','deleteMultiple')" text="Are you sure?"/>
							<gui:actionLink icon="icon-trash" onclick="openModal('confirm-reaction-delete-modal')" hint="Delete"/>
							<gui:actionLink icon="icon-plus-sign" onclick="doTableSelectionAction('acReactionTable','copyMultiple')" hint="Copy"/>
						</g:if>
						<g:else>
							<g:if test="${!instance.speciesSet.getOwnAndInheritedVariables().isEmpty()}">
								<gui:actionLink controller="acReaction" action="create" elementId="acReactionLink" params="['reactionSet.id':instance.id]" hint="Continue here to add reactions" hint-placement="right" hint-show="true"/>
							</g:if>
							<g:else>
								<gui:actionLink controller="acReaction" action="create" elementId="acReactionLink" params="['reactionSet.id':instance.id]" onclick="return checkIfSpeciesExist();" hint="Add New"/>
							</g:else>
						</g:else>
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