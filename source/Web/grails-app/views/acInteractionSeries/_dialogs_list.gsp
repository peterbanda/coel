	<r:script>
		function showCopyMultipleDialog() {
			var ids = getCheckedIds("acInteractionSeriesTable")
			$('#copy-multiple-modal').find('#ids').val(ids.toString())
			$("#copy-multiple-modal" ).modal();
		}

		function showAddInteractionsMultipleDialog() {
			var ids = getCheckedIds("acInteractionSeriesTable")
			$('#add-interactions-multiple-modal').find('#ids').val(ids.toString())

			$.getJSON('${createLink(action: "getInteractionSeriesData")}',
				function(data) {
					$("#additionalInteractionSeriesId").populateKeyValues(data, false);
			});
			$("#add-interactions-multiple-modal" ).modal({ keyboard: true });
		}
	</r:script>

		<gui:modal id="copy-multiple-modal" title="Copy Multiple" action="copyMultiple">
			<g:hiddenField name="ids" value=""/>

			<ui:field label="Recursively">
				<ui:fieldInput>
				    <g:checkBox id="recursively" name="recursively" checked="false"/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>

		<gui:modal id="add-interactions-multiple-modal" title="Add Interactions From" action="addInteractionsMultiple">
			<g:hiddenField name="ids" value="" />

			<div class="row-fluid">
				<div class="span5">
				 	<ui:field label="Source">
						<ui:fieldInput>
							<g:select name="additionalInteractionSeriesId" from=""/>
        	    	    </ui:fieldInput>
                    </ui:field>

 					<ui:field label="Override">
						<ui:fieldInput>
                        	<g:checkBox name="override" checked="${false}"/>
        	    	    </ui:fieldInput>
                    </ui:field>
 				</div>
 			</div>
		</gui:modal>