<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationSeries.label', default: 'Translation Series')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
		<g:render template="editablePart"/>
	</theme:zone>
</body>
</html>