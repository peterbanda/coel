<%@ page import="com.banda.math.task.EvoPopulationContentStoreOption" %>
<%@ page import="com.banda.math.task.EvoPopulationSelection" %>
<%@ page import="com.banda.math.domain.evo.EvoRun" %>
<html>
<head>
	<theme:title>Run Evolution</theme:title>
    <nav:set path="app/evolution/EvolutionRun"/>
	<theme:layout name="main"/>
	<r:script>
        $(document).ready(function() {

			$( "#evoRunSuccess-Dialog" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					OK: function() {
						$( this ).dialog( "close" );
					}
				}
			});
       	});

		function showEvoRunSuccessDialog() {
			$( "#evoRunSuccess-Dialog" ).dialog( "open" );
		}
	</r:script>
<head>
<body>
	<theme:zone name="body">
		<div id="evoRunSuccess-Dialog" title="Evolution Run">
    	     <p>Evolution has been successfully launched.</p>
		</div>
		<div> 
			<ui:form remote="true" name="runEvolutionForm" url="[controller: 'evoRun', action: 'runEvolution']" before="showEvoRunSuccessDialog();" >

	            	<ui:field label="Evolution Task">
	            		<ui:fieldInput>
	            			<g:select name="evoTask.id" from="${evoTasks}"
	            				optionKey="id"
	            				optionValue="${{it.id + ' : ' + it.name}}"
	            				value="${instance.evoTask?.id}"
	            				noSelection= "['': '---']"/>
						</ui:fieldInput>
					</ui:field>

	            	<ui:field label="Init Chromosome Id">
	            		<ui:fieldInput>
	            			<g:textField name="initChromosomeId" value="${initChromosomeId}" />
						</ui:fieldInput>
					</ui:field>

					<ui:field label="Evolution Run To Resume">
						<ui:fieldInput>
							<g:select name="evoRun.id" from="${evoRuns}"
								optionKey="id"
								optionValue="${{it.id + ' : ' + it.timeCreated}}"
								value="${instance.evoRun}"
								noSelection= "['': '---']"/>
						</ui:fieldInput>
					</ui:field>

					<ui:field label="Content To Save">
						<ui:fieldInput>
							<g:select name="populationContentStoreOption" from="${EvoPopulationContentStoreOption.values()}"
								value="${instance.populationContentStoreOption}"
								noSelection= "['': 'Select One...']"/>
						</ui:fieldInput>
					</ui:field>

					<ui:field label="Population To Save">
						<ui:fieldInput>
							<g:select name="populationSelection" from="${EvoPopulationSelection.values()}"
								value="${instance.populationSelection}"
								noSelection= "['': 'Select One...']"/>
						</ui:fieldInput>
					</ui:field>

					<ui:field bean="instance" name="autoSave"/>
					<ui:field bean="instance" name="repeat"/>
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