        <r:script>
			function showCopyDialog() {
				$("#copy-modal").modal();
			}

			function showReplaceSpeciesSetDialog() {
				$.getJSON('${createLink(action: "getSpeciesSetsData")}',
					function(data) {
						$("#speciesSetId").populateKeyValues(data, false);
				});
				$("#replace-species-set-modal").modal();
			}

			function showAddInteractionsDialog() {
				$.getJSON('${createLink(action: "getInteractionSeriesData")}',
					function(data) {
						$("#additionalInteractionSeriesId").populateKeyValues(data, false);
				});
				$( "#add-interactions-modal" ).modal({ keyboard: true });
			}
	</r:script>

		<gui:modal id="copy-modal" title="Copy" action="copy">
			<g:hiddenField name="id" value="${instance.id}"/>
			<ui:field label="Recursively">
				<ui:fieldInput>
				    <g:checkBox id="recursively" name="recursively" checked="false"/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>

		<gui:modal id="replace-species-set-modal" title="Replace Species Set" action="replaceSpeciesSet">
			<g:hiddenField name="id" value="${instance.id}"/>
			<g:select name="speciesSetId" from=""/>
		</gui:modal>

		<gui:modal id="add-interactions-modal" title="Add Interactions From" action="addInteractions">
			<g:hiddenField name="id" value="${instance?.id}" />
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