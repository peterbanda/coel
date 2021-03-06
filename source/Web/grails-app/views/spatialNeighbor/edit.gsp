<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNeighbor.label', default: 'Spatial Neighbor')}" scope="request" />
    <nav:set path="app/network/SpatialNeighborhood"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:ref property="parent" textProperty="name"/>
		</f:with>
		<hr>
		<ui:field bean="instance" name="coordinateDiffs"/>
    </theme:zone>
</body>
</html>