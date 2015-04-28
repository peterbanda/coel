<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'spatialNetworkPerformance.label', default: 'Spatial Network Performance')}" scope="request" />
    <nav:set path="app/network/SpatialPerformance"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>
		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#performanceChart").chart( {
		    	fileNameFun : function() {
					return 'spatial_net_performance_' + ${ids.get(ids.size - 1)} + '-' + ${ids.get(0)} + '.csv'
		    	}					    		
			});

			$("#performanceChartDrawAll").prop('checked', true);

			$("#performanceChart").chart("populate", ${chartData});
		});

	</r:script>
</head>
<body>
    <theme:zone name="details">
    	<ui:field label="Ids">
			<ui:fieldInput>${ids}</ui:fieldInput>
		</ui:field>
        <hr>
		<div id="performanceChart" style="float:center">
      	</div>
    </theme:zone>
</body>
</html>