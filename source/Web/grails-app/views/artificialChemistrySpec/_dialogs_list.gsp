		<r:script>
	
			$(function(){

				// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
				$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
				$( "#dialog-import-form" ).dialog({
					autoOpen: false,
					resizable: true,
					height:320,
					width:450,
					modal: true,
					buttons: {
						Submit: function() {
							$("#importForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
			});

			function showImportDialog() {
				$( "#dialog-import-form" ).dialog( "open" );
			}
		</r:script>

        <div id="dialog-import-form" title="Import as Text">
        	<g:form name="importForm" action="importFromText" method="POST">
        		<table>
            		<tr class="prop">
           				<td valign="top" class="name">
                			<label for="importForm"><g:message code="artificialChemistrySpec.data.label" default="Data" /></label>
	                	</td>
    	            	<td valign="top" class="value">
							<g:textArea name="data" value="" rows="5" cols="70"/>
                		</td>
            		</tr>

            		<tr class="prop">
           				<td valign="top" class="name">
                			<label for="type"><g:message code="artificialChemistrySpec.type.label" default="Type" /></label>
                		</td>
                		<td valign="top" class="value">
                			<g:select name="type" from="${['Symmetric', 'DNS Strand']}"
	                        	noSelection= "['': 'Select One...']" />
    	            	</td>
        	    	</tr>
				</table>
			</g:form>
		</div>