<!DOCTYPE html>
<html>
    <%-- add a body to this head tag to add any meta / common resources --%>
    <theme:head/>
    <theme:body>
        <theme:layoutTemplate name="header"/>

    	<div class="container-fluid">
    		<div class="row-fluid">
    			<div class="span11 content">
	                <div class="page-header">
						<theme:layoutTitle/>
                	</div>

                	<div id="messageDiv" >
    					<g:if test="${flash.message}">
        					<ui:message type="info">${flash.message}</ui:message>
    					</g:if>
    				</div>

	                <div class="row-fluid">
                    	<div class="span11">
                        	<theme:layoutZone name="body"/>
                    	</div>
                	</div>
                </div>
            </div>
            <theme:layoutTemplate name="footer"/>
        </div>
    </theme:body>
</html>