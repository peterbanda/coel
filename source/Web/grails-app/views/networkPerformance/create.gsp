<html>
<head>
	<theme:title>Run Performance Evaluation</theme:title>
	<nav:set path="app/network/Performance"/>
	<theme:layout name="main"/>
	<r:script>
		$(document).ready(function() {

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
	</r:script>
<head>
<body>
	<theme:zone name="body">
		<div id="performanceEvaluationLaunchSuccess-Dialog" title="Performance Evaluation">
			<p>Performance evaluation has been successfully launched.</p>
		</div>
		<div> 
			<ui:form remote="true" name="runPerformanceEvaluationForm" url="[controller: 'networkPerformance', action: 'runPerformanceEvaluation']" before="showPerformanceEvaluationLaunchSuccessDialog();" >
	            <ui:field label="Network">
	           		<ui:fieldInput>
	           			<g:select name="network.id" from="${networks}" optionKey="id"
	           				optionValue="${{it.id + ' : ' + it.name}}"
	           				value="${instance?.network}"
	           				noSelection= "['': 'Select One...']"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field label="Interaction Series">
					<ui:fieldInput>
						<g:select name="actionSeriesIds" from="${actionSeries}" multiple="yes"
						    size="4"
						    optionKey="id"
	           				optionValue="${{it.id + ' : ' + it.name}}"
	           				value="${instance?.networkActionSeries}"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field label="Evaluation">
					<ui:fieldInput>
						<g:select name="evaluationIds" from="${evaluations}" multiple="yes"
						    size="4"
						    optionKey="id" 
	           				optionValue="${{it.id + ' : ' + it.name}}"
	           				value="${instance?.networkEvaluations}"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="runTime"/>
				<ui:field bean="instance" name="repetitions"/>

				<ui:field label="FP Detector Periodicty">
					<ui:fieldInput>
						<g:textField name="fpDetectorPeriodicity" value="25" />
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