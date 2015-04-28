<%@ page import="com.banda.math.domain.StatsType" %>
	<r:script>
		$(function(){

			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
			$( "#dialog:ui-dialog" ).dialog( "destroy" );

			$( "#dialog-export-as-csv-form" ).dialog({
				autoOpen: false,
				resizable: false,
				width:530,
				height:560,
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

        <div id="dialog-export-as-csv-form" title="Export Perturbation Performance">	
			<g:form name="exportAsCSVForm" action="exportAsCSV" method="POST">
				<table>
					<tr class="prop">
						<td valign="top" class="name">
							<label for="perturbationPerformanceIds"><g:message code="acPerturbationPerformanceExportDialog.perturbationPerformance.label" default="Selection" /></label>
						</td>
						<td valign="top" class="value"">
							<g:select name="perturbationPerformanceIds" from="${list}"
								optionKey="id"
								size="10" 
								multiple="yes"/>
						</td>
					</tr>

    	    		<tr class="prop">
                	    <td valign="top" class="name">
                    	    <label for="acId"><g:message code="acPerturbationPerformanceExportDialog.ac.label" default="Artificial Chemistry" /></label>
                       	</td>
                        <td valign="top" class="value">
                        	<g:select name="acId" from="${acs}"
                            	optionKey="id"
                                optionValue="${{it.id + ' : ' + it.name}}"
    							noSelection= "['': '---']"/>
                        </td>
                    </tr>
                    <tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="actionSeriesIds"><g:message code="acPerturbationPerformanceExportDialog.actionSeries.label" default="Interaction Series" /></label>
                        </td>
                        <td valign="top" class="value">
                        	<g:select name="actionSeriesIds" from="${actionSeries}"
                            	optionKey="id"
                                optionValue="${{it.id + ' : ' + it.name}}"
                                size="10"
                                multiple="yes" />
                        </td>
                    </tr>

                	<tr class="prop">
                    	<td valign="top" class="name">
                        	<label for="statsType"><g:message code="acPerturbationPerformanceExportDialog.statsType.label" default="Merge Stats Type" /></label>
                         </td>
                         <td valign="top" class="value">
                         	<g:select name="statsType" from="${StatsType?.values()}" noSelection= "['': '---']"/>
                         </td>
                   	</tr>
				</table>
			</g:form>
		</div>
