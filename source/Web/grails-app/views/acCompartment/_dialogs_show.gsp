    <r:script>
			function showImportRatesDialog() {
				$("#import-rates-modal").modal();
			}

			function showAddExistingSubCompartmentDialog() {
				$("#add-existing-subcompartment-modal").modal();
			}
	</r:script>

		<gui:modal id="import-rates-modal" title="Import Reaction and Permeability Rates" action="importRatesFromText">
			<g:hiddenField name="id" value="${instance.id}"/>
			<ui:field label="Rates">
				<ui:fieldInput>
					<g:textArea name="ratesInput" value="" rows="5" cols="70"/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>

		<gui:modal id="add-existing-subcompartment-modal" title="Add Existing Sub Compartment" action="addSubCompartment">
			<g:hiddenField name="id" value="${instance.id}"/>
			<ui:field label="Sub Compartments">
				<ui:fieldInput>
					<g:select name="subCompartmentId"
						from="${acCompartments}"
						optionKey="id"
						optionValue="${{it.id + " : " + it.label}}"
						noSelection= "['': 'Select One...']"/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>