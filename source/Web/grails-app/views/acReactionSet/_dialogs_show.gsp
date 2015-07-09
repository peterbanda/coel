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
            	$.getJSON('${createLink(action: "getInteractionSeriesData")}?id=' + ${instance?.id},
					function(data) {
					    if (Object.keys(data).length > 0) {
					        var alertDiv = $('<div class="alert alert-block alert-info">');
                            var message = 'The initial concentrations of the following referenced interaction series will be transformed to match DNA SD<br/>'
                            alertDiv.append(message)

						    $.each(data, function(k, v) {
    		    				var outerDiv = $('<div class="control-group">');
                                outerDiv.append('<label for="" class="control-label">' + v + '</label>');

                                var innerDiv = $('<div class="controls">');
                                innerDiv.append('<input name="_interactionSeriesId.' + k + '" type="hidden"><input name="interactionSeriesId.' + k + '" checked="checked" id="interactionSeriesId.' + k + '" type="checkbox">');
                                outerDiv.append(innerDiv);
                                alertDiv.append(outerDiv)
                            });
                            $('#copy-as-dsd-modal-interactionSeries').html("<hr/>");
                            $('#copy-as-dsd-modal-interactionSeries').append(alertDiv);
                        } else {
                            $('#copy-as-dsd-modal-interactionSeries').html("");
                        }
                        $('#copy-as-dsd-modal').modal();
                });
            }

            function showAddReactionsDialog() {
                $.getJSON('${createLink(action: "getReactionSetsData")}',
					function(data) {
						$("#additionalReactionSetId").populateKeyValues(data, false);
						$('#add-reactions-modal').modal({ keyboard: true });
				});
            }

			function showReplaceSpeciesSetDialog() {
				$.getJSON('${createLink(action: "getSpeciesSetsData")}',
					function(data) {
						$("#speciesSetId").populateKeyValues(data, false);
					    $('#replace-species-set-modal').modal({ keyboard: true });
				});
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

		<gui:modal id="copy-as-mass-action-modal" title="Transform to Mass-action" action="createCatalysisMassActionSubstReactionSet">
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
                <ui:field label="Source Reaction Set">
                    <ui:fieldInput>
 					    <g:select name="additionalReactionSetId" from=""/>
                    </ui:fieldInput>
                </ui:field>
                <ui:field label="New Species Set">
                    <ui:fieldInput>
 					    <g:checkBox id="createNewSpeciesSet" name="createNewSpeciesSet" checked="true"/>
                    </ui:fieldInput>
                </ui:field>
 			</div>
		</gui:modal>

		<gui:modal id="copy-as-dsd-modal" title="Transform to DNA SD" action="copyAsDNASD">
			<g:hiddenField name="id" value="${instance?.id}" />

            <div class="row-fluid">
                <ui:field label="C_max">
                    <ui:fieldInput>
                        <g:textField name="CMax" value="0.00001"/>
                    </ui:fieldInput>
                </ui:field>

                <ui:field label="New Compartment">
                    <ui:fieldInput>
                        <g:checkBox name="createNewCompartment" checked="true"/>
                    </ui:fieldInput>
                </ui:field>

                <div id="copy-as-dsd-modal-interactionSeries">
                </div>
            </div>
		</gui:modal>

		<gui:modal id="scale-forward-rates-modal" title="Scale Forward Rate Constants" action="scaleForwardRateConstants">
			<g:hiddenField name="id" value="${instance?.id}" />

            <div class="row-fluid">

                    <ui:field label="Unimolecular">
                        <ui:fieldInput>
                            <g:textField name="uniScale" value="0.0005"/>
                            <div class="pull-right">
                                1/x
                                <g:checkBox style="margin-top:0px;" id="uniScaleInverse" name="uniScaleInverse" checked="false">1/x</g:checkBox>
                            </div>
                        </ui:fieldInput>
                    </ui:field>

                    <ui:field label="Bimolecular">
                        <ui:fieldInput>
                            <g:textField name="biScale" value="500000"/>
                            <div class="pull-right">
                                1/x
                                <g:checkBox style="margin-top:0px;" id="biScaleInverse" name="biScaleInverse" checked="false">1/x</g:checkBox>
                            </div>
                        </ui:fieldInput>
                    </ui:field>

                    <ui:field label="New Reaction Set (Copy)">
                        <ui:fieldInput>
                            <g:checkBox id="createNewReactionSet" name="createNewReactionSet" checked="false"/>
                        </ui:fieldInput>
                    </ui:field>
            </div>
		</gui:modal>
