    <r:script>
			function showSbmlImportDialog() {
				$("#sbml-import-modal").modal();
			}
	</r:script>

		<gui:modal id="sbml-import-modal" title="Import SBML" action="importSbml" method="POST" enctype="multipart/form-data">
			<ui:field label="File">
				<ui:fieldInput>
					<input type="file" id="sbmlFile" name="sbmlFile"/>
				</ui:fieldInput>
			</ui:field>

			<ui:field label="Resolve errors quietly (if possible)">
				<ui:fieldInput>
					<g:checkBox name="ignoreErrorsQuietly" value="${false}" />
				</ui:fieldInput>
			</ui:field>
		</gui:modal>