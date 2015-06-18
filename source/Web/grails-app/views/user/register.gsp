<html>
<head>
	<theme:title>Register User</theme:title>
    <nav:set path="app"/>
	<theme:layout name="dialog"/>
</head>
<body>
    <theme:zone name="body">
        <div id="messageDiv" >
    		<g:if test="${flash.message}">
        <ui:message type="info">${flash.message}</ui:message>
    		</g:if>
    	</div>
        <div id="errorDiv" >
			<g:eachError bean="${instance}" var="error">
				<ui:message type="error"><g:message error="${error}"/></ui:message>
			</g:eachError>
        </div>

    	<ui:form action="registerSave" class="form-dense">
			<ui:field bean="instance" name="username"/>
			<ui:field bean="instance" name="password">
				<ui:fieldInput>
					<g:field type="password" name="password" value=""/>
				</ui:fieldInput>
			</ui:field>
			<g:render template="editablePart"/>
			<ui:actions>
				<ui:button kind="submit" mode="primary">Submit</ui:button>
                <a class="btn btn-cancel" href="${g.createLink(uri:'/')}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</a>
			</ui:actions>
		</ui:form>
    </theme:zone>
</body>
</html>