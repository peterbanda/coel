<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReaction.label', default: 'Reaction')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:ref bean="instance" property="reactionSet">${it.label}</f:ref>
		<hr>
		<g:render template="editablePart"/>
		<ui:field bean="instance" name="enabled"/>
		<g:render template="structureImage"/>
    </theme:zone>
</body>
</html>