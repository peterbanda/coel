<html>
    <head>
        <theme:title>Chemistry Home</theme:title>
        <theme:layout name="main"/>
        <r:script>
        	function showTutorial() {
				$('#tutorial-modal').modal();
			}
        </r:script>
    <head>
    <body>
    	<theme:zone name="dialogs">
			<gui:modal id="no-compartment-modal" title="No Compartment">
				<p> No compartment/chemistry to launch. First define one.</p>
			</gui:modal>
        	<gui:modal id="no-interaction-series-modal" title="No Interaction Series">
				<p> No interaction series to launch. First define one.</p>
			</gui:modal>
			<g:render template="tutorial"/>
    	</theme:zone>
    	<theme:zone name="body">
			<p>Chemistry module is the key part of COEL framework. The functionality includes definition of artificial chemistry,
			interaction series, and translation series, simulation execution, and performance evaluation. Brief documentation can be found 
       		<a href="http://arxiv.org/abs/1407.4027">here</a>.</p>
       		<p>A step-by-step tutorial can be found <a href="javascript:void(0);" onclick="showTutorial();">here</a>.</p>
			<p>The common work-flow is described by the following steps.</p>

			<div class="accordion" id="accordion" style="margin: 25px 0px 0px 0px;">
  				<div class="accordion-group">
    				<div class="accordion-heading">
    					<div class="row-fluid">
        					<div class="pull-right span5">
        						<a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Read more...</a>
        					</div>
        					<div class="span7">
        						<ui:h3>1.
        							<g:if test="${compartmentsEmpty}">
        								<a href="${g.createLink(uri:'/acCompartment/create')}">Model Chemistry</a>
        							</g:if>
        							<g:else>
        								<a href="${g.createLink(uri:'/acCompartment/list')}">Model Chemistry</a>
        							</g:else>
        						</ui:h3>
        					</div>
        				</div>
    				</div>
    				<div id="collapseOne" class="accordion-body collapse">
      					<div class="accordion-inner">
      						<div class="row-fluid" style="margin: 20px 20px 20px 20px;">
      							<div class="span5">
      							  	<p> Artificial chemistry consists of molecular species and reactions paired with rate constants.</p>

      							  	<p> The state of the system is represented by a vector of species concentrations.
      							    Besides ordinary reactions, chemistry allows to define catalytic or inhibitory reactions 
      							    controlled by one or several catalysts and/or inhibitors.</p>

									<p>
  									Chemistry also supports a hierarchical tree-like compartmentalization,
  									where each compartment hosts an independent reaction set, and compartments communicate
  									with each other through a channel-mediated exchange of molecular species. </p>
      							</div>
      							<div class="span5 offset1">
      								<ui:image uri="/images/chemistry-example.png" alt="Chemistry Model"/>
      							</div>
      						</div>
      					</div>
      				</div>
    			</div>
  				<div class="accordion-group">
    				<div class="accordion-heading">
    					<div class="row-fluid">
        					<div class="pull-right span5">
        						<a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Read more...</a>
        					</div>
        					<div class="span7">
        						<ui:h3> 2.
        						    <g:if test="${compartmentsEmpty}"> 
        								<a href="javascript:void(0);" onclick="$('#no-compartment-modal').modal();">Define Interaction Series</a>
        							</g:if>
        							<g:else>
        								<g:if test="${acActionSeriesEmpty}"> 
        									<a href="${g.createLink(uri:'/acInteractionSeries/create')}">Define Interaction Series</a>
        								</g:if>
        								<g:else>
        									<a href="${g.createLink(uri:'/acInteractionSeries/list')}">Define Interaction Series</a>
        								</g:else>
        							</g:else>
        						</ui:h3>
        					</div>
        				</div>
    				</div>
    				<div id="collapseTwo" class="accordion-body collapse">
      					<div class="accordion-inner">
      						<div class="row-fluid" style="margin: 20px 20px 20px 20px;">
      							<div class="span5">
  									<p> Interaction series specifies how the system state, i.e., the concentration of specific species that usually represents the input,
  									is perturbed over time. <p>
  									
  									<p> 
  									An interaction emulates a step in the execution of an experimental protocol, where at a certain time
  									the person performing the chemical experiment injects or removes substances into or from a tank.
  									It is modeled by instantaneously changing the concentration of a species.</p>

  									<p>
  									Concentrations can be modified multiple times, not just initially. For iterative processes, it is useful to define
  									repetitive interactions, in which a sequence of interactions repeat in a loop at predefined time intervals.
  									Interaction specification supports custom variables and <i>JEP</i> expressions referencing current species concentrations and variable values.
  									COEL Interaction Series API is in fact a scripted language that enables to cover a lot of complicated experimental scenarios without actually
  									touching the code in dynamic and safe way (a basic expression validation provided). </p>
      							</div>
      							<div class="span5 offset1">
      								<ui:image uri="/images/interaction-series-example.png" alt="Interaction Series"/>
      							</div>
      						</div>
      					</div>
      				</div>
    			</div>

  				<div class="accordion-group">
    				<div class="accordion-heading">
    					<div class="row-fluid">
        					<div class="pull-right span5">
        						<a class="accordion-toggle" data-toggle="collapse" href="#collapseThree">Read more...</a>
        					</div>
        					<div class="span7">
        						<ui:h3> 3.
        						    <g:if test="${compartmentsEmpty}"> 
        								<a href="javascript:void(0);" onclick="$('#no-compartment-modal').modal();">Define Translation Series (Optional)</a>
        							</g:if>
        							<g:else>
	        							<g:if test="${acTranslationSeriesEmpty}"> 
    	    								<a href="${g.createLink(uri:'/acTranslationSeries/create')}">Define Translation Series (Optional)</a>
        								</g:if>
        								<g:else>
        									<a href="${g.createLink(uri:'/acTranslationSeries/list')}">Define Translation Series (Optional)</a>
        								</g:else>
        							</g:else>
        						</ui:h3>
        					</div>
        				</div>
    				</div>
    				<div id="collapseThree" class="accordion-body collapse">
      					<div class="accordion-inner">
      						<div class="row-fluid" style="margin: 20px 20px 20px 20px;">
      							<div class="span5">
  									<p> COEL’s basic interpretive tool is the 'translation series', defined by the user
  									in a similar manner to interaction series, i.e., it could be periodic as well.
  									A single translation is an interpretation defined as a function of the current concentrations
  									and any predefined constants, and can be Boolean or numeric in its output.</p>

									<p> One can simply plot the output of a translation series to see the CRN’s behavior
									through a certain lens, or use the series as the basis of evaluation and optimization.</p>
      							</div>
      							<div class="span5 offset1">

      							</div>
      						</div>
      					</div>
      				</div>
    			</div>

  				<div class="accordion-group">
    				<div class="accordion-heading">
    					<div class="row-fluid">
        					<div class="pull-right span5">
	        					<a class="accordion-toggle" data-toggle="collapse" href="#collapseFour">Read more...</a>
    	    				</div>
        					<div class="span7">
        						<ui:h3> 4.
        						    <g:if test="${compartmentsEmpty}"> 
        								<a href="javascript:void(0);" onclick="$('#no-compartment-modal').modal();">Launch Chemistry</a>
        							</g:if>
        							<g:else>
        								<a href="${g.createLink(uri:'/acRun')}">Launch Chemistry</a>
        							</g:else>
        						</ui:h3>
        					</div>
        				</div>
    				</div>
    				<div id="collapseFour" class="accordion-body collapse">
      					<div class="accordion-inner">
							<div class="row-fluid" style="margin: 20px 20px 20px 20px;">
								<div class="span5">
	  								<p> Chemistry is executed against a selected interaction series.</p>
	  								<p> User can inspect the execution outcome, i.e., the species' concentration traces, in the embedded chart.
  									If further post-processing is required full or filtered data could be exported into CSV file.</p>
  								</div>
  								<div class="span5 offset1">
									<ui:image uri="/images/concentration-chart.png" alt="Concentration Chart"/>
  								</div>
  							</div>
      					</div>
      				</div>
    			</div>
    		</div>
        </theme:zone>
    </body>
</html>