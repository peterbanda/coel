<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNeighborhood.label', default: 'Spatial Neighborhood')}" scope="request" />
    <nav:set path="app/network/SpatialNeighborhood"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>

	        <hr>

			<f:display property="name"/>
		</f:with>

		<g:render template="neighbors"/>
    </theme:zone>
</body>
</html>