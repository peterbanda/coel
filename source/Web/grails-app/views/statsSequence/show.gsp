<%@ page import="com.banda.math.domain.StatsSequence" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <g:applyLayout name="${layout}" />
        <g:set var="domainNameLabel" value="${message(code: 'statsSequence.label', default: 'Stats Sequence')}" />
        <title><g:message code="default.show.label" args="[domainNameLabel]" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[domainNameLabel]" /></g:link></span>
        </div>
        <div id="pageBody">
            <h1><g:message code="default.show.label" args="[domainNameLabel]" /></h1>
            <div id="messageDiv" >
            	<g:if test="${flash.message}">
            		<div class="message" role="status">${flash.message}</div>
            	</g:if>
            </div>
            <div class="dialog">
                <table>
                	<tbody>
						<f:with bean="statsInstance">
        					<f:display property="id"/>
                            <f:display property="timeCreated"/>

	                        <tr>
    	                    	<td colspan="2">
        	                        <hr style="color: #ccc; background-color: #ccc; height: 1px; width: 100%; border: none;"/>
            	                </td>
                	        </tr>
						</f:with>

                        <tr class="prop">
                            <td valign="top" class="name">
                            	<label for="stats"><g:message code="statsSequence.stats.label" default="Stats" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: statsSequenceInstance, field: 'stats', 'errors')}">
                               	<div class="list" style="width: 550px">
                					<table>
                    					<thead>
                        					<tr>
                        						<th><g:message code="stats.index.label" default="Index" /></th>
                        					                        
												<th><g:message code="stats.mean.label" default="Mean" /></th>
                        
												<th><g:message code="stats.standardDeviation.label" default="Deviation" /></th>
                        					</tr>
                    					</thead>
                    					<tbody>
                    						<g:each in="${statsSequenceInstance?.stats}" status="i" var="statsInstance">
                        						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                        	    					<td><g:link controller="stats" action="show" id="${statsInstance.id}">${fieldValue(bean: statsInstance, field: "index")}</g:link></td>

                            						<td>${fieldValue(bean: statsInstance, field: "mean")}</td>						                       

                            						<td>${fieldValue(bean: statsInstance, field: "standardDeviation")}</td>						                       
                            					</tr>
                    						</g:each>
                    					</tbody>
                					</table>
            					</div>
                            </td>
                    	</tr>
    	       		</tbody>
                </table>
            </div>
            <div class="buttons">
            </div>
        </div>
    </body>
</html>