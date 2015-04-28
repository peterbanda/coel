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
					return 'net_derrida_analysis_' + ${instance.id} + '.csv'
		        }					    		
			});

			$("#derridaChartDrawAll").prop('checked', true);
			$("#derridaChart").chart("populate", ${chartData});
		});

	</r:script>
    
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
            <hr>
			<f:ref property="network" textProperty="name"/>
			<f:display property="repetitions"/>
			<f:display property="runTime"/>
        </f:with>

        <hr>

		<div id="derridaChart" style="float:center">
      	</div>

    </theme:zone>
</body>
</html>