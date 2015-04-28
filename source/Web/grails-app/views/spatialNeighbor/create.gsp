<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNeighbor.label', default: 'Spatial Neighbor')}" scope="request" />
    <nav:set path="app/network/SpatialNeighborhood"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="parent.id" value="${instance.parent.id}" />
		<f:ref bean="instance" property="parent" textProperty="name"/>
		<hr>
		<ui:field bean="instance" name="coordinateDiffs"/>
    </theme:zone>
</body>
</html>