    <r:script>

			function showAddReactionsDialog() {
				var ids = getCheckedIds("acReactionSetTable")
				$('#add-reactions-multiple-modal').find('#ids').val(ids.toString())

				$.getJSON('${createLink(action: "getReactionSetsData")}',
					function(data) {
						$("#additionalReactionSetId").populateKeyValues(data, false);
				});
				$( "#add-reactions-multiple-modal" ).modal({ keyboard: true });
			}

	</r:script>

		<gui:modal id="add-reactions-multiple-modal" title="Add Reactions From" action="addReactionsMultiple">
			<g:hiddenField name="ids" value=""/>

			<div class="row-fluid">
				<div class="span5">
 					<g:select name="additionalReactionSetId" from=""/>
 				</div>
 				<div class="span5 offset1">
 					<g:message code="acReactionSet.createNewSpeciesSet.label" default="Create New Species Set" />
 					<g:checkBox id="createNewSpeciesSet" name="createNewSpeciesSet" checked="false">Create New Species Set</g:checkBox>
 				</div>
 			</div>
		</gui:modal>