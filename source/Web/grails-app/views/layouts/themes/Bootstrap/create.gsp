<!DOCTYPE html>
<html>
	<theme:title>
		<g:message code="default.create.label" args="[domainNameLabel]" />
	</theme:title>
	<theme:head/>
    <r:script>
    	$("[rel=popover]").popover();
    	$("[hint-show=true]").popover('show');
    </r:script>
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
						<g:eachError bean="${instance}" var="error">
							<ui:message type="error"><g:message error="${error}"/></ui:message>
						</g:eachError>
            		</div>

	                <div class="row-fluid">
    	                <div class="span11">
    	                	<theme:ifNoZoneContent name="form">
        	                	<ui:form action="save" class="form-dense">
            	            		<theme:layoutZone name="details"/>
       								<ui:actions>
       									<ui:button type="submit" kind="button" mode="primary">${message(code: 'default.button.create.label', default: 'Create')}</ui:button>
                                        <theme:ifNoZoneContent name="cancelButton">
       									    <ui:button kind="anchor" action="list">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
                                        </theme:ifNoZoneContent>
                                        <theme:ifZoneContent name="cancelButton">
                                            <theme:layoutZone name="cancelButton"/>
                                        </theme:ifZoneContent>
            						</ui:actions>
                				</ui:form>
                			</theme:ifNoZoneContent>
                			<theme:ifZoneContent name="form">
             					<theme:layoutZone name="form"/>
             				</theme:ifZoneContent>
                    	</div>
                	</div>
            	</div>
        	</div>
        	<theme:layoutTemplate name="footer"/>
    	</div>
    </theme:body>
</html>