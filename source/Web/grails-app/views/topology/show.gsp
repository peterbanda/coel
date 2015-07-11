<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'topology.label', default: 'Topology')}" scope="request" />
    <nav:set path="app/network/Topology"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="actions">
		<g:render template="dialogs_show"/>
    	<gui:actionButton action="list"/>
		<span class="dropdown">
			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
				<i class="icon-plus"></i>
				<span class="caret"></span>
			</ui:button>
			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				<li role="presentation"><a role="menuitem" href="${createLink(action: 'createSpatial')}">Spatial</a></li>
				<li role="presentation"><a role="menuitem" href="${createLink(action: 'createTemplate')}">Template</a></li>
				<li role="presentation"><a role="menuitem" href="${createLink(action: 'createLayered')}">Layered</a></li>
			</ul>
		</span>
    	<gui:actionButton action="edit" id="${instance?.id}"/>
		<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
		<gui:actionButton action="copy" hint="Copy" id="${instance?.id}"/>
		<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
			<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
			<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
		</g:if>
		<g:if test="${helpMessage != ''}">
			<gui:actionButton hint="Help" onclick="openModal('help-modal'); return false;" text="?"/>
		</g:if>
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
			<g:render template="type"/>

			<hr>
 
			<f:display property="name"/>

			<g:if test="${instance?.isTemplate()}">
				<f:display property="nodesNum"/>
				<f:display property="layersNum"/>
				<f:display property="nodesPerLayer"/>
				<f:display property="generateBias"/>
				<f:display property="allEdges"/>
				<f:display property="intraLayerAllEdges"/>
				<f:display property="inEdgesNum"/>
				<f:display property="intraLayerInEdgesNum"/>
				<f:display property="allowSelfEdges"/>
				<f:display property="allowMultiEdges"/>
			</g:if>

			<g:if test="${instance?.isSpatial()}">
				<f:display property="sizes">${StringUtils.join(instance.sizes, ', ')}</f:display>
				<f:display property="torusFlag"/>
				<f:display property="metricsType"/>
				<f:display property="radius"/>
				<f:display property="itsOwnNeighor"/>
				<f:ref property="neighborhood" textProperty="name"/>
			</g:if>

			<g:render template="parents"/>
			<g:render template="layers"/>
		</f:with>
    </theme:zone>
</body>
</html>