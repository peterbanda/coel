<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReaction.label', default: 'Reaction')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <theme:layout name="create"/>
    <r:script>
        function showExpressionHelp() {
            $('#jep-help-modal').modal();
        }
    </r:script>
</head>
<body>
    <theme:zone name="details">
        <g:render template="/ac/jep_help"/>
    	<g:hiddenField name="reactionSet.id" value="${instance.reactionSet.id}" />
        <g:hiddenField name="sortOrder" value="${instance?.sortOrder}" />
		<f:ref bean="instance" property="reactionSet">${it.label}</f:ref>
        <hr>
		<g:render template="editablePart"/>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acReactionSet" action="show" id="${instance.reactionSet.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>