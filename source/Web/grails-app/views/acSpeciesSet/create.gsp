<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSpeciesSet.label', default: 'Species Set')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
    	<ui:field bean="instance" name="name"/>
    	<ui:field bean="instance" name="parentSpeciesSet" from="${acSpeciesSets}" optionValue="${{it.id + ' : ' + it.name}}" required="${false}"/>
    </theme:zone>
</body>
</html>