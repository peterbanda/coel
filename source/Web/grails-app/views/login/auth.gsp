<html>
<head>
    <theme:title>Login</theme:title>
    <nav:set path="user/login"/>
    <theme:layout name="dialog"/>
</head>
<body>
    <theme:zone name="body">
        <g:if test="${flash.error_message}">
        	<ui:message type="error">${flash.error_message}</ui:message>
    	</g:if>

   		<ui:form method="POST" action="j_spring_security_check">
	    	<ui:field name="j_username" label="Username"/>
    	    <ui:field name="j_password" type="password" label="Password"/>
    	    <ui:actions>
            	<ui:button mode="primary">Login</ui:button>
               	<ui:button mode="cancel">Cancel</ui:button>
            </ui:actions>
        </ui:form>

		<div class="well">
			Don't have an account?
			<a href="${g.createLink(controller:'user', action: 'register')}">
                <span>Register</span>
			</a>
		</div>
    </theme:zone>
</body>
</html>