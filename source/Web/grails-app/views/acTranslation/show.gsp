<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslation.label', default: ' Translation')}" scope="request" />
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
			<f:ref property="translationSeries" textProperty="name" />
			<hr>			
			<g:if test="${instance.toTime}">
				<f:display property="id" label="Type">Range</f:display>
				<f:display property="fromTime"/>
				<f:display property="toTime"/>
			</g:if>
			<g:else>
				<f:display property="id" label="Type">Point</f:display>
				<f:display label="Apply Time" property="fromTime"/>
			</g:else>
        </f:with>
        <g:render template="translationItems"/>
    </theme:zone>
</body>
</html>