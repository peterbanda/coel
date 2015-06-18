<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<%@ page import="grails.util.GrailsNameUtils" %>
<!DOCTYPE html>
<html>
	<theme:title>
		<g:message code="default.edit.label" args="[domainNameLabel]" />
	</theme:title>
	<theme:head/>
    <r:script>
	    $("[rel=popover]").popover();
	    $("[hint-show=true]").popover('show');
    </r:script>
    <theme:body>
        <theme:layoutTemplate name="header"/>

		<g:set var="domainName" value="${GrailsNameUtils.getPropertyNameRepresentation(GrailsNameUtils.getShortName(instance.class))}" scope="request" />
		<g:set var="helpMessage" value="${message(code: domainName + '.help', default:'')}" scope="request" />

		<g:if test="${helpMessage != ''}">
			<gui:modal id="help-modal" title="Help">
				${helpMessage}
			</gui:modal>
		</g:if>

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

        			<ui:block>
        				<ui:h4 text="Actions"/>
        				<div class="row-fluid">
        					<div class="span8">
								<theme:ifNoZoneContent name="actions">
                                    <gui:modal id="confirm-delete-modal" title="Delete" action="delete">
                                        Are you sure?
                                        <g:hiddenField name="id" value="${instance?.id}"/>
                                    </gui:modal>
    								<gui:actionButton action="list" hint="New"/>
    								<gui:actionButton action="create" hint="Add New"/>
                                    <gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
    								<gui:actionButton action="copy" hint="Copy" id="${instance?.id}"/>
    								<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
	    								<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
    									<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
									</g:if>
                                    <g:if test="${helpMessage != ''}">
                                        <gui:actionButton hint="Help" onclick="openModal('help-modal'); return false;" text="?"/>
                                    </g:if>
								</theme:ifNoZoneContent>
								<theme:ifZoneContent name="actions">
             						<theme:layoutZone name="actions"/>
             					</theme:ifZoneContent>
             				</div>
             				<div class="span4">
             					<theme:ifZoneContent name="extras">
             						<theme:layoutZone name="extras"/>
             					</theme:ifZoneContent>
             				</div>
             			</div>
             		</ui:block>

	                <div class="row-fluid">
    	                <div class="span11">
        	            	<ui:h4 text="Details"/>
        	                <ui:form action="update" class="form-dense">
                				<g:hiddenField name="id" value="${instance?.id}" />
                				<g:hiddenField name="version" value="${instance?.version}" />
            	            	<theme:layoutZone name="details"/>
       							<ui:actions>
       								<ui:button type="submit" mode="primary">${message(code: 'default.button.update.label', default: 'Update')}</ui:button>
       								<ui:button kind="anchor" action="show" id="${instance?.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
            					</ui:actions>
                			</ui:form>
                    	</div>
                	</div>
            	</div>
        	</div>
        	<theme:layoutTemplate name="footer"/>
    	</div>
    </theme:body>
</html>