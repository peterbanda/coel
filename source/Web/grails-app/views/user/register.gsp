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
		            	<g:hasErrors bean="${instance}">
        		    		<ul class="errors" role="alert">
								<g:eachError bean="${instance}" var="error">
									<ui:message type="error"><g:message error="${error}"/></ui:message>
								</g:eachError>
							</ul>
            			</g:hasErrors>
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
				<ui:button type="submit" kind="button" mode="primary">Submit</ui:button>
				<ui:button kind="anchor" href="/">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
			</ui:actions>
		</ui:form>
    </theme:zone>
</body>
</html>