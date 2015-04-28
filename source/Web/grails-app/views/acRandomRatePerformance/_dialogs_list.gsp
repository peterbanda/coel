<%@ page import="com.banda.chemistry.domain.AcRandomRatePerformance" %>

	<r:script>
		$(function(){

			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$( "#dialog:ui-dialog" ).dialog( "destroy" );

			$( "#dialog-export-as-csv-form" ).dialog({
				autoOpen: false,
				resizable: false,
				width:230,
				height:360,
				modal: true,
				buttons: {
					Submit: function() {
						$("#exportAsCSVForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});

		function showExportAsCSVDialog() {
			$( "#dialog-export-as-csv-form" ).dialog( "open" );
		}
	</r:script>

        <div id="dialog-export-as-csv-form" title="Export Random Rate Performance">		
			<g:form name="exportAsCSVForm" action="exportAsCSV" method="POST">
				<table>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="randomRatePerformanceIds"><g:message code="acRandomRatePerformanceExportDialog.randomRatePerformance.label" default="Selection" /></label>
						</td>
						<td valign="top" class="value"">
							<g:select name="randomRatePerformanceIds" from="${AcRandomRatePerformance.list(sort: 'id')}"
								multiple="yes" optionKey="id" size="16" />
						</td>
					</tr>
				</table>
			</g:form>
		</div>