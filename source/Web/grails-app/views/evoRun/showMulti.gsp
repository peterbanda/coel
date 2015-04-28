<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'evoRun.label', default: 'Evolution Run')}" scope="request" />
    <nav:set path="app/evolution/EvolutionRun"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>
		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#fitnessChart").chart( {
		    	fileNameFun : function() {
					return 'evo_run_' + ${ids.get(ids.size - 1)} + '-' + ${ids.get(0)} + '.csv'
		    	}					    		
			});

			$("#fitnessChartDrawAll").prop('checked', true);
			$("#fitnessChart").chart("populate", ${chartData});
		});

	</r:script>
</head>
<body>
    <theme:zone name="details">
    	<ui:field label="Ids">
			<ui:fieldInput>${ids}</ui:fieldInput>
		</ui:field>
        <hr>
		<div id="fitnessChart" style="float:center">
      	</div>
    </theme:zone>
</body>
</html>