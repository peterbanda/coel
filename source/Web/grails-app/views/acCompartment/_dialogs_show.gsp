    <r:script>

			$(function(){

				$( "#dialog-rates-form" ).dialog({
					autoOpen: false,
					resizable: true,
					height:250,
					modal: true,
					buttons: {
						Submit: function() {
							$("#ratesForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#dialog-add-existing-subcompartment-form" ).dialog({
					autoOpen: false,
					resizable: true,
					height:150,
					modal: true,
					buttons: {
						Submit: function() {
							$("#addExistingSubCompartmentForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
			});

			function showImportRatesDialog() {
				$( "#dialog-rates-form" ).dialog( "open" );
			}

			function showAddExistingSubCompartmentDialog() {
				$( "#dialog-add-existing-subcompartment-form" ).dialog( "open" );
			}
	</r:script>

        <div id="dialog-rates-form" title="Import reaction and permeability rates as text">		
			<g:form name="ratesForm" action="importRatesFromText" id="${instance.id}" method="POST">
				<g:textArea name="ratesInput" value="" rows="5" cols="70"/>
			</g:form>
		</div>
        <div id="dialog-add-existing-subcompartment-form" title="Add Existing Sub Compartment">		
			<g:form name="addExistingSubCompartmentForm" action="addSubCompartment" method="POST">
			    <g:hiddenField name="id" value="${instance?.id}" />
                <g:select name="subCompartmentId"
                	from="${acCompartments}"
                	optionKey="id"
                	optionValue="${{it.id + " : " + it.label}}"
                	noSelection= "['': 'Select One...']"/>
			</g:form>
		</div>