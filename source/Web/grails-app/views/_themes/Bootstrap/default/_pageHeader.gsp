    	<r:script>
    		var panels = 2;

			$(document).ready(function(){
				if ('${session["sidebar"]}' == 'false') {
					hideSidebar();
					panels = 1;
				}
			});

    		function hideSidebar() {
      			$('#contentDiv').removeClass('span10 contentDiv');
      			$('#contentDiv').addClass('contentDiv no-sidebar');
      			$('#sidebar').removeClass('menu span2');
      			$('#sidebar').addClass('menu');
      			$('#sidebar').hide();
    			$('#toggleSidebar i').removeClass('icon-chevron-left');
    			$('#toggleSidebar i').addClass('icon-chevron-right');
      		};

      		function showSidebar() {
      			$('#contentDiv').removeClass('contentDiv no-sidebar');
      			$('#contentDiv').addClass('span10 contentDiv');
      			$('#sidebar').removeClass('menu');
      			$('#sidebar').addClass('menu span2');
      			$('#sidebar').show();
      			$('#toggleSidebar i').addClass('icon-chevron-left');
    			$('#toggleSidebar i').removeClass('icon-chevron-right');
			};

			function switchSidebar() {
  				if (panels === 1) {
    				showSidebar();
    				$.post('${createLink(controller: "user", action: "setSidebar")}');
    				panels = 2;
  				} else {
    				hideSidebar();
    				$.post('${createLink(controller: "user", action: "unsetSidebar")}');
    				panels = 1;
  				}
  			};

    	</r:script>  	

	                <div class="page-header">
						<table>
        	        		<tr>
        	        			<td>    	        				
        	        				<a id="toggleSidebar" href="javascript:void(0)" onclick="switchSidebar();" title="Hide/show sidebar navigation">
        	        					<div class="well" style="margin: 0px 10px 0px 0px;">
        	        						<i class="icon-chevron-left"></i>
        	        					</div>
        	        				</a>
<!-- 
										<ui:button id="toggleSidebar" onclick="switchSidebar();">
	                    					<i class="icon-chevron-left"></i>
	                    				</ui:button>
-->

        	        			</td>
            	    			<td>
									<g:render template="/_fields/module"/>
                				</td>
                				<td>
                					<div style="margin: 0px 0px 0px 10px;">
                						<theme:layoutTitle/>
                					</div>
                				</td>
                			</tr>
                		</table>
                	</div>