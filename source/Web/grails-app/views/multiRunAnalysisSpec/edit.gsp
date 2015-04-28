<%@ page import="com.banda.math.domain.dynamics.MultiRunAnalysisSpec" %>
<%@ page import="com.banda.math.domain.dynamics.SingleRunAnalysisSpec" %>
<%@ page import="com.banda.math.domain.rand.RandomDistributionType" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:applyLayout name="${layout}" />
        <g:set var="domainNameLabel" value="${message(code: 'multiRunAnalysisSpec.label', default: 'Multi Analysis Spec')}" />
        <title><g:message code="default.edit.label" args="[domainNameLabel]" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[domainNameLabel]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[domainNameLabel]" /></g:link></span>
        </div>
        <div id="pageBody">
            <h1><g:message code="default.edit.label" args="[domainNameLabel]" /></h1>
            <div id="messageDiv" >
            	<g:if test="${flash.message}">
            		<div class="message">${flash.message}</div>
            	</g:if>
            </div>
            <g:hasErrors bean="${multiRunAnalysisSpecInstance}">
            	<ul class="errors" role="alert">
					<g:eachError bean="${multiRunAnalysisSpecInstance}" var="error">
						<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${multiRunAnalysisSpecInstance?.id}" />
                <g:hiddenField name="version" value="${multiRunAnalysisSpecInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                    
                            <tr class="prop">
                            	<td valign="top" class="name"><g:message code="multiRunAnalysisSpec.id.label" default="Id" /></td>
                            
                            	<td valign="top" class="value">${fieldValue(bean: multiRunAnalysisSpecInstance, field: "id")}</td>
                        	</tr>
                                        
                        	<tr class="prop">
                            	<td valign="top" class="name"><g:message code="multiRunAnalysisSpec.timeCreated.label" default="Time Created" /></td>
                            
	                            <td valign="top" class="value"><g:formatDate date="${multiRunAnalysisSpecInstance?.timeCreated}" /></td>                        
                        	</tr>

                        	<tr>
                            	<td colspan="2">
                                	<hr style="color: #ccc; background-color: #ccc; height: 1px; width: 100%; border: none;"/>
                            	</td>
                        	</tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="multiRunAnalysisSpec.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: multiRunAnalysisSpecInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${multiRunAnalysisSpecInstance?.name}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="singleRunSpec"><g:message code="multiRunAnalysisSpec.singleRunSpec.label" default="Per Run Spec" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: multiRunAnalysisSpecInstance, field: 'singleRunSpec', 'errors')}">
                                    <g:select name="singleRunSpec.id" from="${SingleRunAnalysisSpec.list(sort: 'id')}"
                                    	optionKey="id"
                                    	optionValue="${{it.id + ' : ' + it.name}}"
                                    	value="${multiRunAnalysisSpecInstance?.singleRunSpec?.id}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="runNum"><g:message code="multiRunAnalysisSpec.runNum.label" default="Number of Runs" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: multiRunAnalysisSpecInstance, field: 'runNum', 'errors')}">
                                    <g:textField name="runNum" value="${fieldValue(bean: multiRunAnalysisSpecInstance, field: 'runNum')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="initialStateDistributionType"><g:message code="multiRunAnalysisSpec.initialStateDistributionType.label" default="Initial State Distribution Type" /></label>
                                </td>
                                <td valign="top" class="value">
                                    <g:select name="initialStateDistributionType" from="${RandomDistributionType?.values()}"
                                    	keys="${RandomDistributionType?.values()}"
                                    	value="${multiRunAnalysisSpecInstance?.initialStateDistribution?.getType()}"
                                    	onchange="${remoteFunction(
            								action:'updateRandomDistribution',
            								update:'initialStateDistributionDiv', 
           									params:'\'initialStateDistributionType=\' + this.value+\'&prefix=initialState\'' )}"/>
                                </td>
                            </tr>

							<tr class="prop" id="initialStateDistributionDiv">
                            	<g:render template="/randomDistribution/refresh" model="['randomDistribution':multiRunAnalysisSpecInstance.initialStateDistribution,'prefix':'initialState']" />
							</tr>
                   
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
