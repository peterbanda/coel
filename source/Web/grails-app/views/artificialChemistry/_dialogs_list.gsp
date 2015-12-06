	<r:script>
			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore! 
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
        $(document).ready(function() {

			$( "#dialog-sbml-import-form" ).dialog({
				autoOpen: false,
				resizable: false,
				width:400,
				height:180,
				modal: true,
				buttons: {
					Submit: function() {
						$("#sbmlFileForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});

			$( "#dialog-generate-acs-form" ).dialog({
				autoOpen: false,
				resizable: false,
				width:420,
				height:230,
				modal: true,
				buttons: {
					Submit: function() {
						$("#generateACsForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});
       	});

		function showSBMLDialog() {
			$( "#dialog-sbml-import-form" ).dialog( "open" );
		}

		function showGenerateACsDialog() {
			$( "#dialog-generate-acs-form" ).dialog( "open" );
		}
	</r:script>

    	<div id="dialog-sbml-import-form" title="Import SBML">		
			<g:form name="sbmlFileForm" action="importSbml" method="POST" enctype="multipart/form-data">
				<input type="file" id="sbmlFile" name="sbmlFile"/>
			</g:form>
		</div>
		<div id="dialog-generate-acs-form" title="Generate ACs">		
			<g:form name="generateACsForm" action="generateACs" method="POST">
				<table>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="artificialChemistrySpecId"><g:message code="generateACsDialog.artificialChemistrySpec.label" default="Spec" /></label>
						</td>
						<td valign="top" class="value">
							<g:select name="artificialChemistrySpecId" from="${com.banda.chemistry.domain.ArtificialChemistrySpec.list(sort: 'id')}"
								optionKey="id"
								optionValue="${{it.id + ' : ' + it.name}}"
								noSelection= "['': 'Select One...']"/>
						</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name">
							<label for="acSimulationConfigId"><g:message code="generateACsDialog.acSimulationConfig.label" default="Config" /></label>
						</td>					
						<td valign="top" class="value">
							<g:select name="acSimulationConfigId" from="${com.banda.chemistry.domain.AcSimulationConfig.list(sort: 'id')}"
								optionKey="id"
								optionValue="${{it.id + ' : ' + it.name}}"
								noSelection= "['': 'Select One...']"/>
						</td>
					</tr>

					<tr class="prop">
						<td valign="top" class="name">
							<label for="acNum"><g:message code="generateACsDialog.acNum.label" default="Number of ACs" /></label>
						</td>
						<td valign="top" class="value">
							<g:textField name="acNum" value="20" />
						</td>
					</tr>
				</table>
			</g:form>
		</div>