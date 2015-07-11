<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood')}" scope="request" />
    <nav:set path="app/network/SpatialNeighborhood"/>
	<r:require module="editable"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
		</f:with>

		<hr>

		<ui:field bean="instance" name="name"/>
		<g:render template="neighbors"/>
    </theme:zone>
</body>
</html>