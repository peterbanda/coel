    <r:script>
			function showExportDialogTransitionTableDialog() {
				$('#export-transition-table-modal').modal({ keyboard: true });
			}
	</r:script>

		<gui:modal id="export-transition-table-modal" title="Export Transition Table" action="exportTransitionTable">
			<g:hiddenField name="id" value="${instance?.id}" />
            <ul class="inline">
                <li><div class="spacedTop">Least Significant Bit First</div></li>
                <li><g:checkBox id="lsbfirst" name="lsbfirst" checked="true"></g:checkBox></li>
            </ul>
		</gui:modal>