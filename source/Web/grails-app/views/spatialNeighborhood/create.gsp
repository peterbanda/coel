<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood')}" scope="request" />
    <nav:set path="app/network/SpatialNeighborhood"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="name"/>
    </theme:zone>
</body>
</html>