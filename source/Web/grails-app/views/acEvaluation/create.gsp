<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acEvaluation.label', default: 'Evaluation')}" scope="request" />
    <nav:set path="app/chemistry/Evaluation"/>
    <theme:layout name="create"/>
    <r:script>
        function showExpressionHelp() {
            $('#jep-help-modal').modal();
        }
    </r:script>
</head>
<body>
    <theme:zone name="details">
        <g:render template="/ac/jep_help"/>
        <ui:field bean="instance" name="name"/>
        <ui:field bean="instance" name="translationSeries" from="${translationSeries}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
        <ui:field bean="instance" name="evalFunction">
        	<ui:fieldInput>
            	<g:textField name="evalFunction.formula" value="" />
                <gui:actionButton hint="Help" onclick="showExpressionHelp(); return false;" text="?"/>
            </ui:fieldInput>
        </ui:field>
        <ui:field bean="instance" name="periodicTranslationsNumber"/>
    </theme:zone>
</body>
</html>