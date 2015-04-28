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
					height:280,
					modal: true,
					buttons: {
						Submit: function() {
							$("#displayAnalysisResultsForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#dialog-export-analysis-results-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:280,
					modal: true,
					buttons: {
						Submit: function() {
							$("#exportAnalysisResultsForm").submit();
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

			function showExportAnalysisResultsDialog() {
				$( "#dialog-export-analysis-results-form" ).dialog( "open" );
			}		
	</r:script>

		<div id="dialog-display-analysis-results-form" title="Display Analysis Results">		
			<g:formRemote name="displayAnalysisResultsForm" url="[controller: 'artificialChemistrySpec', action:'displayAnalysisResults']" update="PlotImageDiv">
			    <g:hiddenField name="id" value="${artificialChemistrySpecInstance?.id}" />
				<table>
                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="firstStatsType"><g:message code="artificialChemistrySpec.firstStatsType.label" default="First Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="firstStatsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                         </td>
                   	</tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="singleRunAnalysisResultType"><g:message code="artificialChemistrySpec.singleRunAnalysisResultType.label" default="Result Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="singleRunAnalysisResultType" from="${SingleRunAnalysisResultType?.values()}"
                         		value="${SingleRunAnalysisResultType?.DerridaResults}"
                         		noSelection= "['': 'Select One...']"/>
                         </td>
                   	</tr>

                    <tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="yRangeMax"><g:message code="artificialChemistrySpec.yRangeMax.label" default="Y Range Max" /></label>
                        </td>
                        <td valign="top" class="value">
                        	<g:textField name="yRangeMax" value="" />
                       	</td>                            
                    </tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="secondStatsType"><g:message code="artificialChemistrySpec.secondStatsType.label" default="Second (Merge) Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="secondStatsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                         </td>
                   	</tr>

                    <tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="mergeFlag"><g:message code="artificialChemistrySpec.mergeFlag.label" default="Merge?" /></label>
                        </td>
                        <td valign="top" class="value">
                        	<g:checkBox name="mergeFlag" checked="${false}" />
                       	</td>                            
                    </tr>

				</table>
			</g:formRemote>
		</div>
		<div id="dialog-export-analysis-results-form" title="Export Analysis Results">		
			<g:form name="exportAnalysisResultsForm" action="exportAnalysisResults">
			    <g:hiddenField name="id" value="${artificialChemistrySpecInstance?.id}" />
				<table>
                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="firstStatsType"><g:message code="artificialChemistrySpec.firstStatsType.label" default="First Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="firstStatsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                         </td>
                   	</tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="singleRunAnalysisResultType"><g:message code="artificialChemistrySpec.singleRunAnalysisResultType.label" default="Result Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="singleRunAnalysisResultType" from="${SingleRunAnalysisResultType?.values()}"
                         		value="${SingleRunAnalysisResultType?.DerridaResults}"
                         		noSelection= "['': 'Select One...']"/>
                         </td>
                   	</tr>

                    <tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="yRangeMax"><g:message code="artificialChemistrySpec.yRangeMax.label" default="Y Range Max" /></label>
                        </td>
                        <td valign="top" class="value">
                        	<g:textField name="yRangeMax" value="" />
                       	</td>                            
                    </tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="secondStatsType"><g:message code="artificialChemistrySpec.secondStatsType.label" default="Second (Merge) Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="secondStatsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                         </td>
                   	</tr>
				</table>
			</g:form>
		</div>