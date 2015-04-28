<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslation.label', default: ' Translation')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
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
			<f:ref property="translationSeries" textProperty="name" />
		</f:with>
        <hr>
		<g:render template="editablePart"/>
        <g:render template="translationItems"/>
    </theme:zone>
</body>
</html>