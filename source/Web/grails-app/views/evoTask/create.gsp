<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoAcTask.label', default: 'Evo Task')}" scope="request" />
    <nav:set path="app/evolution/EvolutionTask"/>
    <r:require module="select"/>
    <theme:layout name="create"/>
	<r:script>
    	$(document).ready(function(){
			updateAcDependents();
	   	});
    </r:script>
</head>
<body>
    <theme:zone name="details">
		<g:render template="editablePart"/>
    </theme:zone>
</body>
</html>