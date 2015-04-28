<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationItem.label', default: 'Translation Item')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="edit" id="${instance?.id}"/>
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
			<f:ref property="translation">
				<g:if test="${it.toTime}">
					${it.fromTime + '-' + it.toTime}
				</g:if>
				<g:else>
					${it.fromTime}
				</g:else>
			</f:ref>
			<hr>                                        
        	<f:display property="variable.label"/>
            <f:display property="translationFunction">
            	${render(template:'displayTranslationFunction', bean:instance)}
            </f:display>
        </f:with>
    </theme:zone>
</body>
</html>