<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationItem.label', default: 'Translation Item')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <g:hiddenField name="translation.id" value="${instance.translation.id}" />    
		<f:ref bean="instance" property="translation">${it.fromTime + '-' + it.toTime}</f:ref>
		<hr>                                        
        <ui:field label="Variable ← Function">
        	<ui:fieldInput>
				<g:textField name="variableLabel" value="${instance.variable?.label}" />
				&larr;
				<g:textArea class="input-xxlarge" rows="4" name="translationFunction.formula" value="${render(template:'displayTranslationFunction', bean:instance)}" />
			</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>