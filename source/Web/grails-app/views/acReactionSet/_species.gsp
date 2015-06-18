    <r:script>

		$(function(){
			$("#labels").hide()

    		$("#main").onEnter( function(event) {
    			// to prevent submission of the main form
    			event.preventDefault();
    		});

    		$("#labels").onEnter( function(event) {
				$.post('${createLink(controller: "acSpecies", action: "saveAjax")}',
						{'parentSet.id': ${instance.speciesSet?.id}, labels: $('#labels').val()},
						addSpeciesItems) 

    			event.preventDefault();
    		});
		});

		function addSpeciesLabelTextField() {
			$("#labels").val('');
			$("#labels").show();
			$("#labels").focus();
		}

		function addSpeciesItems(data) {
			if ($("#speciesLabels:contains('None')").length == 1) {
				$("#speciesLabels").html($('<li>Species</li>'));
			}
			$("#addSpeciesLink").data('popover').options.content = 'Add New';
			$("#addSpeciesLink").data('popover').options.placement = 'bottom';
			$.each(data.acSpeciesInstances, function(index, acSpeciesInstance) {			
				var newItem = $('<li><a href="#" id="label' + acSpeciesInstance.id + '" class="plain" data-type="textdeletable" data-pk="' + + acSpeciesInstance.id + '" data-mode="inline" data-url="/acSpecies/updatePropertyAjax" data-title="Enter Label">' + acSpeciesInstance.label + '</a></li>')
				$("#speciesLabels").append(newItem);
				$('#label' + acSpeciesInstance.id).editable({
    				params: function(params) {
    					var data = {};
    					data['id'] = params.pk;
    					data['property'] = 'label';
    					data['value'] = params.value;
    					return data;
  					},
  					deleteFun: function() {deleteSpecies(acSpeciesInstance.id)}
				});
	      	});

			$("#labels").hide();
			hideErrors()
			showMessage(data.message);
		}

		function deleteSpecies(id) {
 			$.post('${createLink(controller: "acSpecies", action: "deleteAjax")}',
				{'id': id},
				function(data) {
					var speciesLabel = $("#speciesLabels").find('#label' + id).parent()
					speciesLabel.remove()
					hideErrors()
					showMessage(data)
				}
			)
			.fail(function(data) {
				hideMessages()
				showErrorMessage(data.responseText);
			})
		} 
	</r:script>

			<ul class="inline">
				<li>
      				<ul id="speciesLabels" class="inline">
      					<li>Species</li>
      					<g:if test="${!instance.speciesSet.getOwnAndInheritedVariables().isEmpty()}">
      						<g:each in="${instance.speciesSet.getOwnAndInheritedVariables()}" var="species" >
    	  						<li>
									<a href="#" id="label${species.id}" class="plain" data-type="textdeletable" data-pk="${species.id}" data-mode="inline" data-url="/acSpecies/updatePropertyAjax" data-title="Enter Label">
       									${species.label}
       								</a>
       								<r:script>
    	   						    	$(document).ready(function() {
	    									$('#label${species.id}').editable({
    											params: function(params) {
    												var data = {};
    												data['id'] = params.pk;
    												data['property'] = 'label';
    												data['value'] = params.value;
    												return data;
  												},
  												deleteFun: function() {deleteSpecies(${species.id})}
											});
										});
									</r:script>
      							</li>
							</g:each>
						</g:if>
						<g:else>
							<li><i>None</i></li>
						</g:else>
					</ul>
				</li>
				<li>
                    <g:if test="${!instance.speciesSet.getOwnAndInheritedVariables().isEmpty()}">
					    <gui:actionLink icon="icon-plus" onclick="addSpeciesLabelTextField();" elementId="addSpeciesLink" hint="Add New"/>
                    </g:if>
                    <g:else>
                        <gui:actionLink icon="icon-plus" onclick="addSpeciesLabelTextField();" elementId="addSpeciesLink" hint="Start here to add new species" hint-placement="top" hint-show="true"/>
                    </g:else>
					<g:textField name="labels" placeholder="Enter species label(s)"/>
				</li>
			</ul>