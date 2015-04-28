      			<ul class="inline">
      				<li>Variables</li>
      				<g:if test="${!instance.variables.isEmpty()}">
      					<g:each in="${instance.variables}" var="variable" >
    	  					<li>
								<a href="#" id="Label${variable.id}" class="plain" data-type="text" data-pk="${variable.id}" data-mode="inline" data-url="/acTranslationVariable/updateAjax" data-title="Enter Label">
       								${variable.label}
       							</a>
       							<r:script>
       					    		$(document).ready(function() {
    									$('#Label${variable.id}').editable();
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