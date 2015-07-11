<%@ page import="org.apache.commons.lang.StringUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'topology.label', default: 'Topology')}" scope="request" />
    <nav:set path="app/network/Topology"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<g:hiddenField name="parentId" value="${parentId}" />
		<g:render template="type"/>

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
    </theme:zone>
</body>
</html>