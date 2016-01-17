<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationItem.label', default: 'Translation Item')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="create"/>
    <r:script>
    	$("#translationFunction\\.formula").focus();

		function showExpressionHelp() {
			$('#jep-help-modal').modal();
		}
    </r:script>
</head>
<body>
    <theme:zone name="details">
		<g:render template="/ac/jep_help"/>
        <g:hiddenField name="translation.id" value="${instance.translation.id}" />    
		<f:ref bean="instance" property="translation">
			<g:if test="${it.toTime}">
				${it.fromTime + '-' + it.toTime}
			</g:if>
			<g:else>
				${it.fromTime}
			</g:else>
		</f:ref>
		<hr>                                        
        <ui:field label="Variable ← Function">
        	<ui:fieldInput>
				<g:textField name="variableLabel" value="${instance.variable?.label}" />
				&larr;
				<g:textArea class="input-xxlarge" rows="4" name="translationFunction.formula" value="${render(template:'displayTranslationFunction', bean:instance)}" />
				<gui:actionButton hint="Help" onclick="showExpressionHelp(); return false;" text="?"/>
			</ui:fieldInput>
        </ui:field>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acTranslation" action="show" id="${instance.translation.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>