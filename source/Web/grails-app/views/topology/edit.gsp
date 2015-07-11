<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'topology.label', default: 'Topology')}" scope="request" />
    <nav:set path="app/network/Topology"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="edit"/>
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
		</f:with>

		<hr>

		<ui:field bean="instance" name="name"/>

		<g:if test="${instance?.isTemplate()}">
			<ui:field bean="instance" name="nodesNum"/>
			<ui:field bean="instance" name="layersNum"/>
			<ui:field bean="instance" name="nodesPerLayer"/>
			<ui:field bean="instance" name="generateBias"/>
			<ui:field bean="instance" name="allEdges"/>
			<ui:field bean="instance" name="intraLayerAllEdges"/>
			<ui:field bean="instance" name="inEdgesNum"/>
			<ui:field bean="instance" name="intraLayerInEdgesNum"/>
			<ui:field bean="instance" name="allowSelfEdges"/>
			<ui:field bean="instance" name="allowMultiEdges"/>
		</g:if>

		<g:if test="${instance?.isSpatial()}">
			<ui:field bean="instance" name="sizes">
				<ui:fieldInput>
					<g:textField name="sizes" value="${StringUtils.join(instance?.sizes, ', ')}" />
				</ui:fieldInput>
			</ui:field>
			<ui:field bean="instance" name="torusFlag"/>
			<ui:field bean="instance" name="metricsType"/>
			<ui:field bean="instance" name="radius"/>
			<ui:field bean="instance" name="itsOwnNeighor"/>
			<ui:field bean="instance" name="neighborhood" from="${spatialNeighborhoods}" optionKey="id" optionValue="${{it.id + ' : ' + it.name}}"/>
		</g:if>

		<g:render template="parents"/>
		<g:render template="layers"/>

    </theme:zone>
</body>
</html>