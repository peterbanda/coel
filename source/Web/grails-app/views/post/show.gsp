<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'post.label', default: 'Post')}" scope="request" />
    <nav:set path="app"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="title"/>
			<f:display property="slug"/>
			<f:display property="dateCreated"/>
			<f:display property="lastUpdated"/>
			<f:display property="author"/>
    		<f:display property="active"/>

			<hr>
			${instance.content}
		</f:with>
    </theme:zone>
</body>
</html>