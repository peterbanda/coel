<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'chromosome.label', default: 'Chromosome')}" scope="request" />
    <nav:set path="app/evolution/EvolutionRun"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
		<gui:actionButton action="showPrevious" id="${instance?.id}"/>
		<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>

            <hr>

			<f:ref property="population">${it.encodeAsHTML()}</f:ref>
			<f:display property="population.generation"/>
			<f:display property="score"/>
			<f:display property="fitness"/>
			<f:display property="code"/>
		</f:with>
    </theme:zone>
</body>
</html>