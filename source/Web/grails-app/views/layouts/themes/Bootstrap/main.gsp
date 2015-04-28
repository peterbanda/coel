<!DOCTYPE html>
<html>
    <%-- add a body to this head tag to add any meta / common resources --%>
    <theme:head>
        <g:javascript library="jquery" plugin="jquery"/>
    	<r:require module="jquery-ui"/>  	
    </theme:head>
    <theme:body>
        <theme:layoutTemplate name="header"/>

    	<div class="container-fluid">
    		<div class="row-fluid">
    		    <div id="sidebar" class="menu span2">
    				<theme:layoutZone name="secondary-navigation"/>
    			</div>
    			<div id="contentDiv" class="content span10">
    				<theme:layoutZone name="pageHeader"/>

                	<div id="messageDiv" >
    					<g:if test="${flash.message}">
        					<ui:message type="info">${flash.message}</ui:message>
    					</g:if>
    				</div>
					<div id="errorDiv" >
		            	<g:hasErrors bean="${instance}">
        		    		<ul class="errors" role="alert">
								<g:eachError bean="${instance}" var="error">
									<ui:message type="error"><g:message error="${error}"/></ui:message>
								</g:eachError>
							</ul>
            			</g:hasErrors>
            		</div>

	                <div class="row-fluid">
                    	<div class="span11">
                        	<theme:layoutZone name="body"/>
                    	</div>
                	</div>
                	<div class="row-fluid">
                		<div class="span11">
                			<theme:ifZoneContent name="dialogs">
             					<theme:layoutZone name="dialogs"/>
             				</theme:ifZoneContent>
             			</div>                	
                	</div>
                </div>
            </div>
            <theme:layoutTemplate name="footer"/>
        </div>
    </theme:body>
</html>
