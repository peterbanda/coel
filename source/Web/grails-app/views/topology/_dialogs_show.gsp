        <r:script>

			$(function(){

				$( "#dialog-add-existing-layer-form" ).dialog({
					autoOpen: false,
					resizable: true,
					height:150,
					modal: true,
					buttons: {
						Submit: function() {
							$("#addExistingLayerForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
			});

			function showAddExistingLayerDialog() {
				$( "#dialog-add-existing-layer-form" ).dialog( "open" );
			}
		</r:script>

        <div id="dialog-add-existing-layer-form" title="Add Existing Layer">		
			<g:form name="addExistingLayerForm" action="addLayer" method="POST">
			    <g:hiddenField name="id" value="${instance?.id}" />
                <g:select name="layerId"
                	from="${topologies}"
                	optionKey="id"
                	optionValue="${{it.id + " : " + it.name}}"
                	noSelection= "['': 'Select One...']"/>
			</g:form>
		</div>