		<g:if test="${attrs.action || attrs.onclick}">
			<r:script>
				$('#submitButton').click(function(){
              		$('#${id}').modal('hide');
          		});
			</r:script>
        	<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel${id}" aria-hidden="true" style="display:none">
        		<div class="modal-dialog">
    				<div class="modal-content">
    					<div class="modal-header modal-success">
							<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
        					<h4 class="modal-title" id="myModalLabel${id}">
        						<ul class="inline">
        							<li>
        								${title}
        							</li>
        						</ul>
        					</h4>
      					</div>
      					<g:if test="${attrs.controller}">
      						<ui:form controller="${attrs.controller}" action="${attrs.action}" class="form-dense">
      							<div class="modal-body">
    	  							${text}
	      						</div>
	      						<div class="modal-footer">
       								<ui:button id="submitButton" type="submit" kind="button" mode="primary">OK</ui:button>
       								<button type="button" class="btn btn-default" data-dismiss="modal" data-target="#${id}">Close</button>
            					</div>
                			</ui:form>
                		</g:if>
                		<g:elseif test="${attrs.action}">
      						<ui:form action="${attrs.action}" class="form-dense">
      							<div class="modal-body">
    	  							${text}
	      						</div>
	      						<div class="modal-footer">
       								<ui:button id="submitButton" type="submit" kind="button" mode="primary">OK</ui:button>
       								<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            					</div>
                			</ui:form>
                		</g:elseif>
                		<g:else>                	
      						<div class="modal-body">
      							${text}
      						</div>
      						<div class="modal-footer">
      						    <ui:button id="${id}-button" kind="button" onclick="${attrs.onclick}" data-dismiss="modal">OK</ui:button>
        						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      						</div>
                		</g:else>
    				</div>
  				</div>
			</div>			
		</g:if>
		<g:else>
        	<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel${id}" aria-hidden="true" style="display:none">
        		<div class="modal-dialog">
    				<div class="modal-content">
    					<div class="modal-header modal-success">
							<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
        					<h4 class="modal-title" id="myModalLabel${id}">
        						<ul class="inline">
        							<li>
        								<i class="icon-info-sign"></i>
        							</li>
        							<li>
        								${title}
        							</li>
        						</ul>
        					</h4>
      					</div>
      					<div class="modal-body">
      						${text}
      					</div>
      					<div class="modal-footer">
        					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      					</div>
    				</div>
  				</div>
			</div>
		</g:else>