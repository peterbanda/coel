<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'post.label', default: 'Post')}" scope="request" />
    <nav:set path="app"/>
    <theme:layout name="create"/>
	<r:script disposition="head">
		var URL_ROOT = '${request.contextPath}';
    </r:script>
	<r:require module="wysiwyg"/>
</head>
<body>
    <theme:zone name="details">
		<ui:field bean="instance" name="title"/>
		<ui:field bean="instance" name="slug"/>
		<ui:field bean="instance" name="active"/>
		<ui:field bean="instance" name="content">
			<ui:fieldInput>
				<g:textArea name="content" class="wysiwyg">
					${'<b>Hello</b>'}
				</g:textArea>
			</ui:fieldInput>
		</ui:field>
    </theme:zone>
</body>
</html>