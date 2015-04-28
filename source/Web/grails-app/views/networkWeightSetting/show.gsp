<%@ page import="com.banda.network.domain.NetworkWeightSetting" %>
<%@ page import="com.banda.network.domain.TemplateNetworkWeightSetting" %>
<%@ page import="com.banda.network.domain.FixedNetworkWeightSetting" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:applyLayout name="${layout}" />
        <g:set var="domainNameLabel" value="${message(code: 'networkWeightSetting.label', default: 'Network Weight Setting')}" />
        <title><g:message code="default.show.label" args="[domainNameLabel]" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div id="pageBody">
            <h1><g:message code="default.show.label" args="[domainNameLabel]" /></h1>
            <g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
            <div class="dialog">
                <table>
                    <tbody>
						<f:with bean="networkWeightSettingInstance">
        					<f:display property="id"/>
        					<f:display property="timeCreated"/>
							<f:ref property="createdBy" textProperty="username"/>

	                        <tr>
    	                        <td colspan="2">
        	                        <hr style="color: #ccc; background-color: #ccc; height: 1px; width: 100%; border: none;"/>
            	                </td>
                	        </tr>

        					<f:display property="index"/>

	                        <g:if test="${networkWeightSettingInstance?.getClass() == FixedNetworkWeightSetting.class}">
	                        	<f:display property="weightsNum">${value.size()}</f:display>
							</g:if>
	                    	<g:if test="${networkWeightSettingInstance?.getClass() == TemplateNetworkWeightSetting.class}">
	                    		<f:display property="randomDistribution">${value.getType()}</f:display>
	            	        </g:if>

	                        <tr>
    	                    	<td colspan="2">
        	                    	<hr style="color: #ccc; background-color: #ccc; height: 1px; width: 100%; border: none;"/>
            	                </td>
                	        </tr>

							<f:display property="name"/>

    	                    <g:if test="${networkWeightSettingInstance?.getClass() == FixedNetworkWeightSetting.class}">
    	                    	<f:display property="settingOrder"/>
                    	    </g:if>
						</f:with>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${networkWeightSettingInstance?.id}" />
                    <g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </g:form>
            </div>
        </div>
    </body>
</html>
