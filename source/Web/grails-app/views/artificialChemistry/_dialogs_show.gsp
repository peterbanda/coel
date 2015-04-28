<%@ page import="com.banda.chemistry.domain.ArtificialChemistry" %>
<%@ page import="com.banda.math.domain.StatsType" %>
<%@ page import="com.banda.math.domain.dynamics.SingleRunAnalysisResultType" %>

    <r:script>

			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore! 
			$( "#dialog:ui-dialog" ).dialog( "destroy" );

        	$(document).ready(function() {

				$( "#dialog-display-analysis-results-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:230,
					modal: true,
					buttons: {
						Display: function() {
							$("#displayAnalysisResultsButton").click();
							$( this ).dialog( "close" );
						},
						Export: function() {
							$("#export").val("true");
							$("#exportAnalysisResultsButton").click();
							$( this ).dialog( "close" );
						},
						
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#dialog-run-ac-for-analysis-result-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:150,
					modal: true,
					buttons: {
						Submit: function() {
							$("#runAcForAnalysisResultForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#dialog-export-run-for-analysis-result-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:180,
					modal: true,
					buttons: {
						Submit: function() {
							$("#exportRunForAnalysisResultForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
        	});

			function showDisplayAnalysisResultsDialog() {
				$( "#dialog-display-analysis-results-form" ).dialog( "open" );
			}

			function showRunAcForAnalysisResultDialog() {
				$( "#dialog-run-ac-for-analysis-result-form" ).dialog( "open" );
			}

			function showExportRunForAnalysisResultDialog() {
				$( "#dialog-export-run-for-analysis-result-form" ).dialog( "open" );
			}			
	</r:script>

		<div id="dialog-display-analysis-results-form" title="Display Analysis Results">
			<g:form name="displayAnalysisResultsForm" >
			    <g:hiddenField name="id" value="${instance?.id}" />
			    <g:hiddenField name="export" value="false" />
				<table>
                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="statsType"><g:message code="artificialChemistry.statsType.label" default="Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="statsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                         </td>
                   	</tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="singleRunAnalysisResultType"><g:message code="artificialChemistry.singleRunAnalysisResultType.label" default="Result Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="singleRunAnalysisResultType" from="${SingleRunAnalysisResultType?.values()}"
                         		value="${SingleRunAnalysisResultType?.DerridaResults}"
                         		noSelection= "['': 'Select One...']"/>
                         </td>
                   	</tr>
				</table>
                <div class="buttons" hidden="true">
                	<g:submitToRemote id="displayAnalysisResultsButton" class="save" url="[controller: 'artificialChemistry', action:'displayAnalysisResults']"
                		update="PlotImageDiv"
                		value="${message(code: 'button.displayAnalysisResults.label', default: 'Display')}"/>
                    <g:actionSubmit id="exportAnalysisResultsButton" class="save" action="displayAnalysisResults"
                    	value="${message(code: 'button.exportAnalysisResultsAsSVG.label', default: 'Export As SVG')}" />
                </div>
			</g:form>
		</div>
		<div id="dialog-run-ac-for-analysis-result-form" title="Rerun AC For Analysis">		
			<g:formRemote name="runAcForAnalysisResultForm" url="[controller: 'artificialChemistry', action:'runAcForAnalysisResult']" update="PlotImageDiv">
			    <g:hiddenField name="id" value="${instance?.id}" />
				<table>
                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="singleRunResult.id"><g:message code="artificialChemistry.singleRunResult.label" default="Single Run Result" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="singleRunResult.id" from="${singleRunResults}"
                         		optionKey="id"
                         		optionValue="id"
                         		noSelection= "['': 'Select One...']"/>
                         </td>
                   	</tr>
				</table>
			</g:formRemote>
		</div>
		<div id="dialog-export-run-for-analysis-result-form" title="Export Run For Analysis">		
			<g:form name="exportRunForAnalysisResultForm" action="exportRunForAnalysisResult">
			    <g:hiddenField name="id" value="${instance?.id}" />
				<table>
                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="singleRunResult.id"><g:message code="artificialChemistry.singleRunResult.label" default="Single Run Result" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="singleRunResult.id" from="${singleRunResults}"
                         		optionKey="id"
                         		optionValue="id"
                         		noSelection= "['': 'Select One...']"/>
                         </td>
                   	</tr>
				</table>
			</g:form>
		</div>	