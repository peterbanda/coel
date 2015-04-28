<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslation.label', default: ' Translation')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="translationSeries.id" value="${instance.translationSeries.id}" />
    	<f:ref bean="instance" property="translationSeries" textProperty="name" />
        <hr>
        <g:render template="editablePart"/>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acTranslationSeries" action="show" id="${instance.translationSeries.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>