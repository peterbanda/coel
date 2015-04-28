<html>
<head>
	<theme:title>Run Perturbation Performance Evaluation</theme:title>
    <nav:set path="app/chemistry/PerturbationPerformance"/>
	<theme:layout name="main"/>
	<r:script>
        $(document).ready(function() {
	    	$("#compartment\\.id").bind("change", updateCompartmentDependents);
	    	adjustMultiSelectSizes();

			$( "#performanceEvaluationLaunchSuccess-Dialog" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					OK: function() {
						$( this ).dialog( "close" );
					}
				}
			});
       	});

		function showPerformanceEvaluationLaunchSuccessDialog() {
			$( "#performanceEvaluationLaunchSuccess-Dialog" ).dialog( "open" );
		}

		function updateCompartmentDependents() {
			$.getJSON('${createLink(action: "getCompartmentDependentData")}?id=' + $("#compartment\\.id").val(),
				function(data) {
	      			$("#actionSeriesIds").populateKeyValues(data.interactionSeries, false);
	      			$("#evaluationIds").populateKeyValues(data.evaluations, false);
	      			adjustMultiSelectSizes();
				});
		}

		function adjustMultiSelectSizes() {
			adjustSelectSize('actionSeriesIds', 3, 20)
			adjustSelectSize('evaluationIds', 2, 5)
		}

		function adjustSelectSize(selectId, min, max) {
			var newSize = $('select#' + selectId + ' option').length
			if (newSize > max) newSize = max
			if (newSize < min) newSize = min
			$('#' + selectId).attr('size', newSize);
		}
	</r:script>
<head>
<body>
	<theme:zone name="body">
        <div id="performanceEvaluationLaunchSuccess-Dialog" title="Performance Evaluation">
    	     <p>Perturbation performance evaluation has been successfully launched.</p>
		</div>
		<div> 
			<ui:form remote="true" name="runPerformanceEvaluationForm" url="[controller: 'acPerturbationPerformance', action: 'runPerformanceEvaluation']" before="showPerformanceEvaluationLaunchSuccessDialog();" >
	            <ui:field label="Compartment">
	           		<ui:fieldInput>
	           			<g:select name="compartment.id" from="${compartments}" optionKey="id"
	           				optionValue="${{it.id + ' : ' + it.label}}"
	           				value="${instance?.compartment}"
	           				noSelection= "['': 'Select One...']"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field label="Interaction Series">
					<ui:fieldInput>
						<g:select class="input-xxlarge" name="actionSeriesIds" from="" multiple="yes"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field label="Evaluation">
					<ui:fieldInput>
						<g:select name="evaluationIds" from="" multiple="yes"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="runTime"/>
				<ui:field bean="instance" name="repetitions"/>
				<ui:field bean="instance" name="perturbationNum"/>
				<ui:field bean="instance" name="perturbationStrengths"/>
				</hr>
				<ui:field label="Simulation Config">
	            	<ui:fieldInput>
	            		<g:select name="simulationConfigId" class="span6" from="${simulationConfigs}" optionKey="id"
	            			optionValue="${{it.id + ' : ' + it.name}}"
	            			value="${instance.simulationConfig?.id}"
	            			noSelection= "['': 'Select One...']"/>
                    </ui:fieldInput>
                </ui:field>
				<ui:field label="Jobs In Sequence Num">
					<ui:fieldInput>
						<g:textField name="jobsInSequenceNum" value="${instance.jobsInSequenceNum}" />
					</ui:fieldInput>
				</ui:field>

				<ui:field label="Max Jobs In Parallel Num">
					<ui:fieldInput>
						<g:textField name="maxJobsInParallelNum" value="${instance.maxJobsInParallelNum}" />
					</ui:fieldInput>
				</ui:field>
				<ui:actions>
					<ui:button type="submit" kind="button" mode="primary">Run</ui:button>
					<ui:button kind="anchor">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
				</ui:actions>
			</ui:form>
      	</div>
	</theme:zone>
</body>
</html>