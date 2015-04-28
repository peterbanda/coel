<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionGroup.label', default: 'Reaction Group')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
        <gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="reactionSet">${it.encodeAsHTML()}</f:ref>

	        <hr>

			<f:display property="label"/>
			<f:display property="id" label="Forward Rate Constants">${rateConstants.join(', ')}</f:display>
		</f:with>
		<g:render template="reactions"/>
    </theme:zone>
</body>
</html>