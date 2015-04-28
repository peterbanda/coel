    <r:script>

		$(function(){
			$("#labels").hide()

    		$("#main").onEnter( function(event) {
    			// to prevent submission of the main form
    			event.preventDefault();
    		});

    		$("#labels").onEnter( function(event) {
				$.post('${createLink(controller: "acInteractionVariable", action: "saveAjax")}',
						{'parentSet.id': ${instance?.id}, labels: $('#labels').val()},
						addVariables) 

    			event.preventDefault();
    		});
		});

		function addVariableLabelTextField() {
			$("#labels").val('');
			$("#labels").show();
			$("#labels").focus();
		}

		function addVariables(data) {
			if ($("#variableLabels:contains('None')").length == 1) {
				$("#variableLabels").html($('<li>Variables</li>'));
			}
			$.each(data.acInteractionVariableInstances, function(index, acVariableInstance) {			
				var newItem = $('<li><a href="#" id="label' + acVariableInstance.id + '" class="plain" data-type="textdeletable" data-pk="' + + acVariableInstance.id + '" data-mode="inline" data-url="/acInteractionVariable/updatePropertyAjax" data-title="Enter Label">' + acVariableInstance.label + '</a></li>')
				$("#variableLabels").append(newItem);
				$('#label' + acVariableInstance.id).editable({
    				params: function(params) {
    					var data = {};
    					data['id'] = params.pk;
    					data['property'] = 'label';
    					data['value'] = params.value;
    					return data;
  					},
  					deleteFun: function() {deleteVariable(acVariableInstance.id)}
				});
	      	});

			$("#labels").hide();
			hideErrors()
			showMessage(data.message);
		}

		function deleteVariable(id) {
 			$.post('${createLink(controller: "acInteractionVariable", action: "deleteAjax")}',
				{'id': id},
				function(data) {
					var variableLabel = $("#variableLabels").find('#label' + id).parent()
					variableLabel.remove()
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
      				<ul id="variableLabels" class="inline">
      					<li>Variables</li>
      					<g:if test="${!instance.getOwnAndInheritedVariables().isEmpty()}">
      						<g:each in="${instance.getOwnAndInheritedVariables()}" var="variable" >
    	  						<li>
									<a href="#" id="label${variable.id}" class="plain" data-type="textdeletable" data-pk="${variable.id}" data-mode="inline" data-url="/acInteractionVariable/updatePropertyAjax" data-title="Enter Label">
       									${variable.label}
       								</a>
       								<r:script>
    	   						    	$(document).ready(function() {
	    									$('#label${variable.id}').editable({
    											params: function(params) {
    												var data = {};
    												data['id'] = params.pk;
    												data['property'] = 'label';
    												data['value'] = params.value;
    												return data;
  												},
  												deleteFun: function() {deleteVariable(${variable.id})}
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
					<a href="javascript:void(0);" class="create" onclick="addVariableLabelTextField();"/>
						<i class="icon-plus"></i>
					</a>
					<g:textField name="labels" placeholder="Enter variable label(s)"/>
				</li>
			</ul>