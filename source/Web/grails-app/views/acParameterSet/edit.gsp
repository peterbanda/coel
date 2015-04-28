<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acParameterSet.label', default: 'Parameter Set')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
    	<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="speciesSet" textProperty="name"/>
		</f:with>

	    <hr>

		<ui:field bean="instance" name="name"/>

		<g:render template="parameters"/>
    </theme:zone>
</body>
</html>