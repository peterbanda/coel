<%@ page import="com.banda.chemistry.business.MassActionType" %>

    <r:script>

			function showImportRatesDialog() {
				$('#import-rates-modal').modal({ keyboard: true });
			}

			function showExportAsLatexTableDialog() {
				$('#export-as-latex-modal').modal({ keyboard: true });
			}

			function showMassActionCopyDialog() {
				$('#copy-as-mass-action-modal').modal({ keyboard: true });
			}

			function showDNASDCopyDialog() {
				$('#copy-as-dsd-modal').modal();
			}

			function showReplaceSpeciesSetDialog() {
				$.getJSON('${createLink(action: "getSpeciesSetsData")}',
					function(data) {
						$("#speciesSetId").populateKeyValues(data, false);
				});
				$('#replace-species-set-modal').modal({ keyboard: true });
			}

			function showMassActionCopyDialog() {
				$('#copy-as-mass-action-modal').modal({ keyboard: true });
			}

			function showScaleForwardRateConstantsDialog() {
				$( "#scale-forward-rates-modal" ).modal({ keyboard: true });
			}

	</r:script>

		<gui:modal id="import-rates-modal" title="Import Rates" action="importRatesFromText">
			<g:hiddenField name="id" value="${instance?.id}" />
			<g:textArea name="ratesInput" class="input-xlarge" value="" rows="5"/>
		</gui:modal>

		<gui:modal id="export-as-latex-modal" title="Export as LaTeX table" action="exportAsLatexTable">
			<g:hiddenField name="id" value="${instance?.id}" />
			<ul class="inline">
  				<li>Include Rates</li>
  				<li><g:checkBox id="includeRates" name="includeRates" checked="true"></g:checkBox></li>
			</ul>
		</gui:modal>

		<gui:modal id="copy-as-mass-action-modal" title="Copy/Convert to Mass-action" action="createCatalysisMassActionSubstReactionSet">
			<g:hiddenField name="id" value="${instance?.id}" />
			<g:select id="massActionType" name="massActionType" from="${MassActionType?.values()}"/>
		</gui:modal>

		<gui:modal id="replace-species-set-modal" title="Replace Species Set" action="replaceSpeciesSet">
			<g:hiddenField name="id" value="${instance?.id}" />
			<g:select name="speciesSetId" from=""/>
		</gui:modal>

		<gui:modal id="add-reactions-modal" title="Add Reactions From" action="addReactions">
			<g:hiddenField name="id" value="${instance?.id}" />
			<div class="row-fluid">
				<div class="span5">
 					<g:select name="additionalReactionSetId" from=""/>
 				</div>
 				<div class="span5 offset1">
 					<g:message code="acReactionSet.createNewSpeciesSet.label" default="Create New Species Set" />
 					<g:checkBox id="createNewSpeciesSet" name="createNewSpeciesSet" checked="true">Create New Species Set</g:checkBox>
 				</div>
 			</div>
		</gui:modal>

		<gui:modal id="copy-as-dsd-modal" title="Copy/Convert to DSD" action="copyAsDNASD">
			<g:hiddenField name="id" value="${instance?.id}" />
 			<g:message code="acReactionSet.cmax.label" default="CMAX " />
 			<g:textField name="CMax" value="0.00001" />
		</gui:modal>

		<gui:modal id="scale-forward-rates-modal" title="Scale Forward Rate Constants" action="scaleForwardRateConstants">
			<g:hiddenField name="id" value="${instance?.id}" />
			<ul class="inline">
  				<li>Unimolecular</li>
  				<li><g:textField name="uniScale" value="0.0005"/></li>
			</ul>
			<ul class="inline">
  				<li>Bimolecular</li>
  				<li><g:textField name="biScale" value="500000"/></li>
			</ul>
		</gui:modal>
