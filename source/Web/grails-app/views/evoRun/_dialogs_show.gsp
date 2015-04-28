	<r:script>
		$(function(){

			$( "#dialog-import-last-chrom-form" ).dialog({
				autoOpen: false,
				resizable: true,
				height:200,
				width:450,
				modal: true,
				buttons: {
					Submit: function() {
						$("#importLastChromForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});

		function showImportLastBestChromosomeDialog() {
			$( "#dialog-import-last-chrom-form" ).dialog( "open" );
		}

		function showImportLastBestAcInteractionSeriesChromosomeDialog() {
			$("#dialog-import-last-chrom-ac-interaction-series-modal").modal();
		}

	</r:script>

		<gui:modal id="dialog-import-last-chrom-ac-interaction-series-modal" title="Import Last Best Chromosome" action="importLastBestChromosome">
			<g:hiddenField name="id" value="${instance.id}"/>
			<ui:field label="Create New Interaction Series?">
				<ui:fieldInput>
				    <g:checkBox id="createNewInteractionSeries" name="createNewInteractionSeries" checked="true"/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>

        <div id="dialog-import-last-chrom-form" title="Import Last Best Chromosome">		
			<ui:form name="importLastChromForm" action="importLastBestChromosome" method="POST">
			    <g:hiddenField name="id" value="${instance.id}" />

				<ui:field label="Action">
					<ui:fieldInput>
						<g:select name="actionSpec" from="${["Import", "New function", "New network"]}"/>
					</ui:fieldInput>
				</ui:field>
			</ui:form>
		</div>