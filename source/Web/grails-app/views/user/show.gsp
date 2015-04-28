<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'user.label', default: 'User')}" scope="request" />
    <nav:set path="app"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
    	<g:render template="change_password_dialog"/>
    	<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
    	    <gui:actionButton action="list"/>
    		<gui:actionButton action="create"/>
    		<gui:actionButton action="edit" id="${instance?.id}"/>
    		<g:if test="${!instance.accountEnabled}">
    			<gui:actionButton action="activate" id="${instance?.id}" text="Activate"/>
    		</g:if>
		</g:if>
		<ui:button kind="button" onclick="showChangePasswordDialog();"><i class="icon-wrench"></i>Change Password</ui:button>
    	<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
    		<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
	    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    		<gui:actionButton action="showNext" id="${instance?.id}"/>
		</g:if>    	
    </theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="username"/>
			<f:display property="createTime"/>
			<f:display property="changeTime"/>

			<hr>

			<f:display property="firstName"/>
			<f:display property="lastName"/>
			<f:display property="email"/>
			<f:display property="affiliation"/>
			<f:display property="intendedUse"/>
			<f:display property="phoneNumber"/>
			<f:display property="website"/>
			<f:display property="address"/>
			<f:display property="aboutMe"/>
			<sec:ifAnyGranted roles="ROLE_ADMIN">
				<f:display property="accountEnabled"/>
				<f:display property="accountExpired"/>
				<f:display property="accountLocked"/>
				<f:display property="credentialsExpired"/>
				<f:display property="roles"/>
			</sec:ifAnyGranted>
		</f:with>
    </theme:zone>
</body>
</html>