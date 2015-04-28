<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoAcTask.label', default: 'Evo Task')}" scope="request" />
    <nav:set path="app/evolution/EvolutionTask"/>
    <r:require module="select"/>
    <theme:layout name="edit"/>
</head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="list" hint="List"/>
    	<span class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown" hint="Add New">
  				<i class="icon-plus"></i>
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcRateConstantTaskInstance')}">AC Rate Constant Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcInteractionSeriesTaskInstance')}">AC Interaction Series Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcSpecInstance')}">AC Spec Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createNetworkInstance')}">Network Type</a></li>
  			</ul>
		</span>
    	<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" hint="Delete"/>
    	<gui:actionButton action="copy" hint="Copy" id="${instance?.id}"/>
    	<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
	    	<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
    		<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
		</g:if>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
		</f:with>
		<hr>
		<g:render template="editablePart"/>
		<g:render template="evoRuns"/>
    </theme:zone>
</body>
</html>