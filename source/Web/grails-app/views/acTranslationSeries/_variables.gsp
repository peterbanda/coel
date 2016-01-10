      			<ul class="inline">
      				<li>Variables</li>
      				<g:if test="${!instance.variables.isEmpty()}">
      					<g:each in="${instance.variables}" var="variable" >
    	  					<li>
								<a href="#" id="label${variable.id}" class="plain" data-type="text" data-pk="${variable.id}" data-mode="inline" data-url="/acTranslationVariable/updatePropertyAjax" data-title="Enter Label">
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
  											}
  										})
    								});
								</r:script>
      						</li>
						</g:each>
					</g:if>
					<g:else>
						<li>
							<i>None</i>
						</li>
					</g:else>
				</ul>