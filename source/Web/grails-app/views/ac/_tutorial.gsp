		<gui:modal id="tutorial-modal" title="Chemistry Tutorial">
      		<p>This tutorial walks you through the creation and simulation of a CRN in the COEL framework.</p>
      		<p>As an example CRN we will define and simulate the Lotka-Volterra system, a popular chemical oscillator which models predator-prey relations. It contains three molecular species:
      			<ul>
      				<li>
      					G (grass)
      				</li>
      				<li>
      					R (rabbit)
      				</li>
      				<li>
      					F (fox)
      				</li>
      			</ul>
			</p>
      		<p>and three reactions:  			
      			<ul>
      				<li>
      					R &#43; G &rarr; 2 R (Rabbit eats grass)
      				</li>
      				<li>
      					F &#43; R &rarr; 2 F (Fox eats rabbit)
      				</li>
      				<li>
      					F &rarr; &lambda; (Fox dies).
      				</li>
      			</ul>
      		</p>
			<hr>
			<p>As shown in <a href="/ac">Chemistry Home</a> we need to go through 3 (alternatively 4) simple steps.</p>
			<ui:h3>1. Model Chemistry</ui:h3>
			<ul>
				<li>
      				<p>
      					Click [Model Chemistry] in <a href="/ac">Chemistry Home</a> or the [+] button in the <a href="/acCompartment">Compartment</a> view (menu item located on the left).
      				</p>
      			</li>
      			<li>
      				<p>
      					Enter the label, e.g., 'Lotka-Volterra', and press Enter or click [Create].
      					<ui:image uri="/images/tutorial/compartment.png" alt="CRN"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					Define species in the Species section. Click [+] and type 'G, R, F' (with or without commas) and press Enter.
      					<ui:image uri="/images/tutorial/reaction_set.png" alt="Species"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					Define reactions in the Reaction section. Click [+ New reaction] and type 'R + G' &rarr; '2 R' in the two Reaction text fields, set Forward Rate Constants to '0.1', and press Enter. Repeat for the two remaining reactions 'F + R' &rarr; '2 F' and 'F' &rarr; ''. In both cases you can use '0.1' as a forward rate.
      					<ui:image uri="/images/tutorial/reaction.png" alt="Reaction"/>
      				</p>
      				<br>
      				<div class="alert alert-info" role="alert">
      					<h4>Tip</h4>
      					<p>After you finish the tutorial try experimenting with different rate constants.</p>      					
      				</div>
      			</li>
      		</ul>
			<hr>
		    <ui:h3>2. Define Interaction Series</ui:h3>
			<ul>
				<li>
      				<p>
      					Click [Define Interaction Series] in <a href="/ac">Chemistry Home</a> or the [+] button in the <a href="/acInteractionSeries">Interaction Series</a> view (menu item located on the left).
      				</p>
      			</li>
      			<li>
      				<p>
      					Enter the name, e.g., 'Lotka-Volterra', and press Enter or click [Create].
      					<ui:image uri="/images/tutorial/interaction_series.png" alt="Interaction Series"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					Define a species interaction for each species (G, R, F) by clicking [+ Species Assignment] for the initial interaction at time zero, which is created automatically.
      					Enter '2' as a concentration Setting Function for 'F', as well as for 'R', and enter '1' for 'G'. 
      					<ui:image uri="/images/tutorial/species_action.png" alt="Species Action"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					Set species G as immutable, i.e., its concentration remains constant despite being consumed by reaction 'R + G &rarr; 2 R'.
      					Click [Edit icon] in the interaction series (show) view and select 'G' as Immutable Species and submit (Update). 
      					<ui:image uri="/images/tutorial/interaction_series_immutable.png" alt="Immutable Species"/>
      				</p>
      				<br>
      				<div class="alert alert-info" role="alert">
      					<h4>Tip</h4>
      					<p>What happens if we skip this step and keep G (grass) mutable?</p>      					
      				</div>
      				<br>
      				<p>
      			     At the end the interaction series should look like:
      				<ui:image uri="/images/tutorial/interaction_series_final.png" alt="Interaction Series Final"/>
      				</p>
      			</li>
      		</ul>
			<hr>
		    <ui:h3>3. Define Translation Series (Optional)</ui:h3>
			<ul>
				<li>
      				<p>
      					Click [Define Translation Series] in <a href="/ac">Chemistry Home</a> or the [+] button in <a href="/acTranslationSeries">Translation Series</a> view (menu item located on the left).
      				</p>
      			</li>
      			<li>
      				<p>
      					Enter the name, e.g., 'Lotka-Volterra', click [Show Advanced Settings] and set Periodicity to '100' and press Enter or click [Create].
      					<ui:image uri="/images/tutorial/translation_series.png" alt="Translation Series"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					Define a point translation with Apply Time '10' by clicking [+ Translation]. 
      					<ui:image uri="/images/tutorial/translation.png" alt="Translation"/>
      				</p>
      			</li>
      			<li>
      				<p>
      				    Add a translation item ([+ Item]) with Variable 'R10' and Function 'R'.
      					<ui:image uri="/images/tutorial/translation_item.png" alt="Translation Item"/>
      				</p>
      				<br>
      				<div class="alert alert-info" role="alert">
      					<h4>Tip</h4>
      					<p>You can experiment with different translation functions, e.g., '(R + F)/2', or define a range translation and play with aggregate functions, such as 'avg(R)'.</p>
      				</div>
      			</li>
      		</ul>
			<hr>
		    <ui:h3>4. Launch Chemistry</ui:h3>
			<ul>
				<li>
      				<p>
      					Click [Launch Chemistry] in <a href="/ac">Chemistry Home</a> or the <a href="/acRun">Launch Chemistry</a> menu item located on the left.
      				</p>
      			</li>
      			<li>
      				<p>
      					Select the Compartment you defined in step 1 ('Lotka-Volterra') and the Interaction Series you defined in step 2 ('Lotka-Volterra').
      					If you defined a Translation Series in step 3 select it as well. Press Enter or click [Run].
      					<ui:image uri="/images/tutorial/launch_chemistry.png" alt="Launch Chemistry"/>
      				</p>
      			</li>
      			<li>
      				<p>
      					After a while the concentration chart and translation chart (if you defined a Translation series) will appear.
						You can investigate concentration traces of selected species, increase the resolution by adjusting the number of interpolated points, and restrict the plot's domain with the slider at the bottom.  
      					<ui:image uri="/images/tutorial/chemistry_chart.png" alt="Concentration Chart"/>
      				</p>
      			</li>
      		</ul>
            <ui:h3>5. Compile to DNA Strand Displacement</ui:h3>
            <div class="alert alert-info" role="alert">
                <h4>Note</h4>
                <p>Be sure that the reaction set you want to compile contains reactions driven purely by mass-action kinetics (i.e. contains no explicit catalysts or inhibitors and no custom rates).
            </div>
            <ul>
                <li>
                    <p>
                        Open a reaction set you want to work with.
                    </p>
                </li>
                <li>
                    <p>
                        (Optional) Scale unimolecular and bimolecular reactions appropriately. Click [Scale Forward Rates] in Extras menu located on the right. Or copy the selected reaction set first and then scale.
                    </p>
                </li>
                <li>
                    <p>
                        Apply a CRN-to-DNA-Strand-displacement transformation to your reaction set (Soloveichik 2010) by clicking [Copy as DNA SD] in Extras menu located on the right.
                    </p>
                </li>
                <li>
                    <p>
                        To view the structured (DNA strand/domain specified) reactions select [Show Structured Reactions] in Extras menu located on the right.
                    </p>
                </li>
            </ul>
		</gui:modal>
