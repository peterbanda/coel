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
					return 'spatial_net_performance_' + ${instance.id} + '.csv'
		        }					    		
			});

			$("#performanceChartDrawAll").prop('checked', true);
			$("#performanceChart").chart("populate", ${chartData});
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
			<f:ref property="evaluation" textProperty="name"/>
			<f:ref property="interactionSeries" textProperty="name"/>
			<f:display property="repetitions"/>
			<f:display property="runTime"/>
			<f:display property="sizeFrom"/>
			<f:display property="sizeTo"/>
        </f:with>

        <hr>

		<div id="performanceChart" style="float:center">
      	</div>

    </theme:zone>
</body>
</html>