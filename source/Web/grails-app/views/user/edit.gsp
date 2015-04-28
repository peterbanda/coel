<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'user.label', default: 'User')}" scope="request" />
    <nav:set path="app"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="actions">
    	<g:render template="change_password_dialog"/>
    	<sec:ifAnyGranted roles="ROLE_ADMIN">
    	    <gui:actionButton action="list"/>
    		<gui:actionButton action="create"/>
    		<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
	    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    		<gui:actionButton action="showNext" id="${instance?.id}"/>
		</sec:ifAnyGranted>
		<ui:button kind="button" onclick="showChangePasswordDialog();"><i class="icon-wrench"></i>Change Password</ui:button>
    </theme:zone>

    <theme:zone name="details">
    	<g:if test='${changePassword}'>
    		<r:script>
    			showChangePasswordDialog();
    		</r:script>
    	</g:if>
    	<f:with bean="instance">
			<f:display property="username"/>
			<f:display property="createTime"/>
			<f:display property="changeTime"/>
		</f:with>

		<hr>

		<g:render template="editablePart"/>

		<sec:ifAnyGranted roles="ROLE_ADMIN">
			<ui:field bean="instance" name="accountEnabled"/>
			<ui:field bean="instance" name="accountExpired"/>
			<ui:field bean="instance" name="accountLocked"/>
			<ui:field bean="instance" name="credentialsExpired"/>
		</sec:ifAnyGranted>

		<g:render template="roles"/>
    </theme:zone>
</body>
</html>