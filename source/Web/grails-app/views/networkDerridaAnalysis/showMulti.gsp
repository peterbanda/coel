<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkDerridaAnalysis.label', default: 'Network Derrida Analysis')}" scope="request" />
    <nav:set path="app/network/DerridaAnalysis"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>
		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#derridaChart").chart( {
		    	fileNameFun : function() {
					return 'derrida_' + ${ids.get(ids.size - 1)} + '-' + ${ids.get(0)} + '.csv'
		    	}					    		
			});

			$("#derridaChartDrawAll").prop('checked', true);
			$("#derridaChart").chart("populate", ${chartData});
		});

	</r:script>
</head>
<body>
    <theme:zone name="details">
    	<ui:field label="Ids">
			<ui:fieldInput>${ids}</ui:fieldInput>
		</ui:field>
        <hr>
		<div id="derridaChart" style="float:center">
      	</div>
    </theme:zone>
</body>
</html>