<g:if test="${instance.evolutionRuns}">
<hr>
<ui:field bean="instance" name="evolutionRuns">
	<ui:fieldInput>
		<div class="row-fluid span9">
			<gui:table list="${instance.evolutionRuns}" showEnabled="true">
       			<gui:column property="id"/>
				<gui:column property="timeCreated"/>
				<gui:column label="Generation">${bean.lastPopulation?.generation}</gui:column>					
            	<gui:column label="Last Score">
	            	<g:set var="chrom" value="${bean.lastPopulation?.bestOrLastChromosome}"/>
    	        	<g:link controller="chromosome" action="show" id="${chrom?.id}">
        	        	${formatNumber(number: chrom?.score, type: 'number', maxFractionDigits: 2)}
            	    </g:link>
            	</gui:column>
            	<gui:column label="Last Fitness">
	            	<g:set var="chrom" value="${bean.lastPopulation?.bestOrLastChromosome}"/>
    	        	<g:link controller="chromosome" action="show" id="${chrom?.id}">
        	        	${formatNumber(number: chrom?.fitness, type: 'number', maxFractionDigits: 5)}
            	    </g:link>
            	</gui:column>
			</gui:table>
		</div>
	</ui:fieldInput>
</ui:field>
</g:if>