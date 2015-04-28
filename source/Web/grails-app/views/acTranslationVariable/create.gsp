<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationVariable.label', default: 'Translation Variable')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="parentSet.id" value="${instance.parentSet.id}" />
		<f:ref bean="instance" controller="acTranslationSeries" property="parentSet" textProperty="name"/>
		<hr>
        <ui:field bean="instance" name="label"/>
	</theme:zone>
</body>
</html>