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
    	<gui:actionButton action="createTemplate" icon="icon-plus" text="Template"/>
    	<gui:actionButton action="createLayered" icon="icon-plus" text="Layered"/>
    	<gui:actionButton action="createSpatial" icon="icon-plus" text="Spatial"/>    	
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
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
			<ui:field bean="instance" name="innerLayerAllEdges"/>
			<ui:field bean="instance" name="inEdgesNum"/>
			<ui:field bean="instance" name="innerLayerInEdgesNum"/>
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