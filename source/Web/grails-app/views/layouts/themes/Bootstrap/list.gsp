<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<!DOCTYPE html>
<html>
	<theme:title>
		<g:message code="default.list.label" args="[domainNameLabel]" />
	</theme:title>
	<theme:head>
    	<r:require module="filterpane" />
    </theme:head>
    <r:script>
		$("[rel=popover]").popover();
		$("[hint-show=true]").popover('show');

    	function showCopyMultipleDialog() {
			var ids = getCheckedIds("${domainName}Table")
			$('#copy-multiple-modal').find('#ids').val(ids.toString())
			$("#copy-multiple-modal" ).modal();
		}
	</r:script>
    <theme:body>
        <theme:layoutTemplate name="header"/>
    		<div class="row-fluid">
                <div class="container-fluid">
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
						<g:if test="${flash.error}">
							<ui:message type="error">${flash.error}</ui:message>
						</g:if>
					</div>

                    <ui:block>
        				<ui:h4 text="Actions"/>
        				<div class="row-fluid">
        					<div class="span8">
								<gui:modal id="confirm-delete-modal" title="Delete" onclick="doTableSelectionAction('${domainName}Table','deleteMultiple')" text="Are you sure?"/>
								<theme:ifNoZoneContent name="actions">
                                    <g:if test="${list.isEmpty()}">
                                        <gui:actionButton action="create" hint="Add New (Start Here)" hint-show="true" />
                                    </g:if>
                                    <g:else>
                                        <gui:actionButton action="create" hint="Add New"/>
                                    </g:else>
        							<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
        							<gui:actionButton action="copy" hint="Copy" onclick="doTableSelectionAction('${domainName}Table','copyMultiple');return false;"/>
        							<filterpane:filterButton/>
									<theme:ifNoZoneContent name="filter">
										<filterpane:filterPane domain="${className}"/>
									</theme:ifNoZoneContent>
									<theme:ifZoneContent name="filter">
             							<theme:layoutZone name="filter"/>
             						</theme:ifZoneContent>
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
             				<theme:layoutZone name="table"/>
            			</div>
        			</div>
                	<div class="row-fluid">
                    	<div class="span11 pagination-centered">
                    		<g:if test="${!params.fullList}">
                        		<theme:layoutZone name="pagination"/>
                        	</g:if>
                    	</div>
                	</div>
            	</div>
            	<theme:layoutTemplate name="footer"/>
        	</div>
    	</div>
    </theme:body>
</html>