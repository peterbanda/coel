    <r:script>
			function showSbmlImportDialog() {
				$("#sbml-import-modal").modal();
			}
	</r:script>

		<gui:modal id="sbml-import-modal" title="Import SBML" action="importSbml" method="POST" enctype="multipart/form-data">
            <input type="file" id="sbmlFile" name="sbmlFile"/>
		</gui:modal>