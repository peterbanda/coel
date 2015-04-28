<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<!DOCTYPE html>
<html>
	<theme:title>
		<g:message code="default.show.label" args="[domainNameLabel]" />
	</theme:title>
	<theme:head/>
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

        			<ui:block>
        				<ui:h4 text="Actions"/>
        				<div class="row-fluid">
        					<div class="span10">
								<theme:ifNoZoneContent name="actions">
                                    <gui:modal id="confirm-delete-modal" title="Delete" action="delete">
                                        Are you sure?
                                        <g:hiddenField name="id" value="${instance?.id}"/>
                                    </gui:modal>
    								<gui:actionButton action="list" hint="List"/>
    								<gui:actionButton action="create" hint="Add New"/>
    								<gui:actionButton action="edit" id="${instance?.id}" hint="Edit"/>
    								<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
    								<gui:actionButton action="copy" hint="Copy" id="${instance?.id}"/>
    								<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
	    								<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
    									<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
									</g:if>
								</theme:ifNoZoneContent>
								<theme:ifZoneContent name="actions">
             						<theme:layoutZone name="actions"/>
             					</theme:ifZoneContent>
             				</div>
             				<div class="span2">
             					<theme:ifZoneContent name="extras">
             						<theme:layoutZone name="extras"/>
             					</theme:ifZoneContent>
             				</div>
             			</div>
             		</ui:block>

	                <div class="row-fluid">
    	                <div class="span11">
        	                <ui:h4 text="Details"/>
        	                <div class="form-horizontal form-dense">
            	            	<theme:layoutZone name="details"/>
            	            </div>
                    	</div>
                	</div>
            	</div>
        	</div>
        	<theme:layoutTemplate name="footer"/>
    	</div>
    </theme:body>
</html>