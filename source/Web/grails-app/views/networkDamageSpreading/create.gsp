<html>
<head>
	<theme:title>Run Damage Spreading Analysis</theme:title>
	<nav:set path="app/network/DamageSpreading"/>
	<theme:layout name="main"/>
	<r:script>
		$(document).ready(function() {

			$( "#damageSpreadingLaunchSuccess-Dialog" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					OK: function() {
						$( this ).dialog( "close" );
					}
				}
			});
        });

		function showDamageSpreadingLaunchSuccessDialog() {
			$( "#damageSpreadingLaunchSuccess-Dialog" ).dialog( "open" );
		}		
	</r:script>
<head>
<body>
	<theme:zone name="body">
		<div id="damageSpreadingLaunchSuccess-Dialog" title="Damage Spreading">
			<p>Damage spreading analysis has been successfully launched.</p>
		</div>
		<div> 
			<ui:form remote="true" name="runDamageSpreadingForm" url="[controller: 'networkDamageSpreading', action: 'runAnalysis']" before="showDamageSpreadingLaunchSuccessDialog();" >
	            <ui:field label="Network">
	           		<ui:fieldInput>
	           			<g:select name="network.id" from="${networks}" optionKey="id"
	           				optionValue="${{it.id + ' : ' + it.name}}"
	           				value="${instance?.network}"
	           				noSelection= "['': 'Select One...']"/>
					</ui:fieldInput>
				</ui:field>

				<ui:field bean="instance" name="runTime"/>
				<ui:field bean="instance" name="repetitions"/>

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