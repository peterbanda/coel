<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluationItem.label', default: 'Network Evaluation Item')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
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
        	<f:display property="id"/>
			<f:ref property="evaluation" textProperty="name"/>
			<hr>                                        
        	<f:display property="variable.label"/>
            <f:display property="evalFunction">${render(template:'displayEvaluationFunction', bean:instance)}</f:display>
        </f:with>
    </theme:zone>
</body>
</html>