<%@ page import="com.banda.network.domain.NetworkWeightSetting" %>
<%@ page import="com.banda.network.domain.FixedNetworkWeightSetting" %>
<%@ page import="com.banda.network.domain.TemplateNetworkWeightSetting" %>
<%@ page import="com.banda.network.domain.FixedNetworkWeightSettingOrder" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:applyLayout name="${layout}" />
        <g:set var="domainNameLabel" value="${message(code: 'networkWeightSetting.label', default: 'Network Weight Setting')}" />
        <title><g:message code="default.edit.label" args="[domainNameLabel]" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div id="pageBody">
            <h1><g:message code="default.edit.label" args="[domainNameLabel]" /></h1>
            <g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${networkWeightSettingInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${networkWeightSettingInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
            <g:form method="post">
                <g:hiddenField name="id" value="${networkWeightSettingInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                     	   <tr class="prop">
                        	    <td valign="top" class="name"><g:message code="networkWeightSetting.id.label" default="Id" /></td>
                            
                           		<td valign="top" class="value">${fieldValue(bean: networkWeightSettingInstance, field: "id")}</td>
                        	</tr>

                        	<tr class="prop">
                            	<td valign="top" class="name"><g:message code="networkWeightSetting.timeCreated.label" default="Time Created" /></td>
                            
                            	<td valign="top" class="value"><g:formatDate date="${networkWeightSettingInstance?.timeCreated}" /></td>
                        	</tr>

                       	 	<tr class="prop">
                            	<td valign="top" class="name"><g:message code="networkWeightSetting.index.label" default="Index" /></td>
                            
	                        	<td valign="top" class="value">${fieldValue(bean: networkWeightSettingInstance, field: "index")}</td>
    	                	</tr>

                        	<g:if test="${networkWeightSettingInstance?.getClass() == FixedNetworkWeightSetting.class}">
                        		<tr class="prop">
                            		<td valign="top" class="name"><g:message code="networkWeightSetting.weightsNum.label" default="Number of Weights" /></td>
                            
                            		<td valign="top" class="value">${networkWeightSettingInstance.weights.size()}</td>
                        		</tr>
							</g:if>
	                    	<g:if test="${networkWeightSettingInstance?.getClass() == TemplateNetworkWeightSetting.class}">
                        		<tr class="prop">
                            		<td valign="top" class="name"><g:message code="networkWeightSetting.randomDistribution.label" default="Random Distribution" /></td>
                            
                            		<td valign="top" class="value">${networkWeightSettingInstance.randomDistribution?.getType()}</td>
                        		</tr>
            	       		</g:if>

                            <tr>
                                <td colspan="2">
                                	<hr style="color: #ccc; background-color: #ccc; height: 1px; width: 100%; border: none;"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="networkWeightSetting.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: networkWeightSettingInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${networkWeightSettingInstance?.name}" />
                                </td>
                            </tr>

	                        <g:if test="${networkWeightSettingInstance?.getClass() == FixedNetworkWeightSetting.class}">
                            	<tr class="prop">
                                	<td valign="top" class="name">
	                                    <label for="settingOrder"><g:message code="networkWeightSetting.settingOrder.label" default="Setting Order" /></label>
                                	</td>
                                	<td valign="top" class="value ${hasErrors(bean: networkWeightSettingInstance, field: 'settingOrder', 'errors')}">
                                    	<g:select name="settingOrder" from="${FixedNetworkWeightSettingOrder?.values()}"
                                    		keys="${LFInputType?.values()*.name()}"
                                    		value="${networkWeightSettingInstance?.settingOrder?.name()}"
                                    		 noSelection= "['': 'Select One...']" />
                                	</td>
                            	</tr>
            	            </g:if>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </div>
            </g:form>
        </div>
    </body>
</html>
