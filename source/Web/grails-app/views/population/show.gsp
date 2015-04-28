<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'population.label', default: 'Population')}" scope="request" />
    <nav:set path="app/evolution/EvolutionRun"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="actions">
        <gui:actionButton action="importBestChromosome" id="${instance.id}">Import Best Chromosome</gui:actionButton>
    	<gui:actionButton action="showPrevious" id="${instance.id}"/>
    	<gui:actionButton action="showNext" id="${instance.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:display property="timeCreated"/>

	        <hr>

        	<f:display property="generation"/>
        	<f:ref property="evolutionRun">${it?.encodeAsHTML()}</f:ref>
			<f:display property="minScore">${render(template:'displayWorstScore',bean:instance)}</f:display>
			<f:display property="meanScore">${render(template:'displayMeanScore',bean:instance)}</f:display>
            <f:display property="maxScore">${render(template:'displayBestScore',bean:instance)}</f:display>
			<f:display property="minFitness">${render(template:'displayWorstFitness',bean:instance)}</f:display>
			<f:display property="meanFitness">${render(template:'displayMeanFitness',bean:instance)}</f:display>
			<f:display property="maxFitness">${render(template:'displayBestFitness',bean:instance)}</f:display>

			<f:display property="id" label="Last Best Chromosome">
				<g:link controller="chromosome" action="show" id="${instance.bestOrLastChromosome.id}">${instance.bestOrLastChromosome.encodeAsHTML()}</g:link>
			</f:display>
		</f:with>

		<g:render template="chromosomes"/>
    </theme:zone>
</body>
</html>