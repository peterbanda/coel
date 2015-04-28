<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionGroup.label', default: 'Reaction Group')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
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
        	<f:display property="createTime"/>
			<f:ref property="createdBy" textProperty="username"/>
			<f:ref property="reactionSet">${it.encodeAsHTML()}</f:ref>
		</f:with>
	    <hr>
		<ui:field bean="instance" name="label"/>
		<ui:field label="Forward Rate Constants">
			<ui:fieldInput>
				<g:textField name="forwardRateConstants" value="${rateConstants.join(', ')}"/>
			</ui:fieldInput>
		</ui:field>
		<g:render template="reactionsEditable"/>
    </theme:zone>
</body>
</html>