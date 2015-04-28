
				<div class="row-fluid span11">
           		    <gui:table list="${instance?.populations}" showEnabled="true">
       					<gui:column property="generation"/>
       					<gui:column property="timeCreated"/>
						<gui:column label="Mean Score">
							<g:render template="/population/displayMeanScore" bean="${bean}"/>
						</gui:column>
						<gui:column label="Mean Fitness">
							<g:render template="/population/displayMeanFitness" bean="${bean}"/>
						</gui:column>
						<gui:column label="Best Score">
							<g:render template="/population/displayBestScore" bean="${bean}"/>
						</gui:column>
						<gui:column label="Best Fitness">
							<g:render template="/population/displayBestFitness" bean="${bean}"/>
						</gui:column>
        			</gui:table>
            	</div>