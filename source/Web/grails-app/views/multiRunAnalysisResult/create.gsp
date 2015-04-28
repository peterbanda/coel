<%@ page import="com.banda.math.domain.dynamics.MultiRunAnalysisSpec" %>
<%@ page import="com.banda.chemistry.domain.ArtificialChemistry" %>
<html>
<head>
	<theme:title>Run Multi Analysis</theme:title>
    <nav:set path="app/chemistry/MultiRunAnalysisResult"/>
    <theme:layout name="main"/>
    <r:script>

        	$(document).ready(function() {

				$( "#multiRunAnalysisSuccess-Dialog" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						OK: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#derridaAnalysisRerunSuccess-Dialog" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						OK: function() {
							$( this ).dialog( "close" );
						}
					}
				});
        	});

			function showMultiRunAnalysisSuccessDialog() {
				$( "#multiRunAnalysisSuccess-Dialog" ).dialog( "open" );
			}

			function showdDerridaAnalysisRerunSuccessDialog() {
				$( "#derridaAnalysisRerunSuccess-Dialog" ).dialog( "open" );
			}
	</r:script>
</head>
<body>
	<theme:zone name="body">
        <div id="multiRunAnalysisSuccess-Dialog" title="Multi Run Analysis">
         	<p>Analysis has been successfully launched.</p>
		</div>
        <div id="derridaAnalysisRerunSuccess-Dialog" title="Rerun Derrida Analysis">
         	<p>Derrida analysis (rerun) has been successfully launched.</p>
		</div>
		<div>
	    <ui:form remote="true" name="runSimulationForm" url="[controller: 'multiRunAnalysisResult', action: 'launchAnalysis']" before="showMultiRunAnalysisSuccessDialog();">
			<ui:field label="Specificiation">
				<ui:fieldInput>
					<g:select name="multiRunAnalysisSpec" from="${MultiRunAnalysisSpec.list(sort: 'id', order: 'desc')}" optionKey="id"
	        	    	optionValue="${{it.id + ' : ' + it.name}}"
	            		value=""/>
            	</ui:fieldInput>
			</ui:field>

			<ui:field label="Artificial Chemistries">
				<ui:fieldInput>
					<g:select name="acIds" from="${acs}" optionKey="id"
	            		optionValue="${{it.id + ' : ' + it.name}}"
	            		value=""
						multiple="yes"
                    	size="20"/>
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
<!-- 
                	<g:submitToRemote value="Rerun Derrida Analysis" class="save" url="[controller: 'multiRunAnalysisResult', action: 'rerunDerridaAnalysis']" before="showdDerridaAnalysisRerunSuccessDialog();"/>
 -->
