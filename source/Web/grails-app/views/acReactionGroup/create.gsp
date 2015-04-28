<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionGroup.label', default: 'Reaction Group')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="reactionSet.id" value="${instance.reactionSet.id}" />
		<f:ref bean="instance" property="reactionSet">${it.encodeAsHTML()}</f:ref>
	    <hr>
		<ui:field bean="instance" name="label"/>
		<g:render template="reactionsEditable"/>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acReactionSet" action="show" id="${instance.reactionSet.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>