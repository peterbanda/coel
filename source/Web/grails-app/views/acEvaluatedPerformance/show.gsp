<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acEvaluatedPerformance.label', default: 'Evaluated Performance')}" scope="request" />
    <nav:set path="app/chemistry/Performance"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>
		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#performanceChart").chart( {
		    	fileNameFun : function() {
					return 'performance_' + ${instance.id} + '.csv'
		        }					    		
			});

			$("#performanceChartDrawAll").prop('checked', true);

			$.get('${createLink(action: "getChartDataForId", id: instance.id)}',
				function(data) {
					$("#performanceChart").chart("populate", data);
				});

			$("#toggleSidebar").on('click', function () {
				$("#performanceChart").chart("redraw");
			});

 			$(window).resize(function(){
        		$("#performanceChart").chart("redraw");
    		});
		});

	</r:script>
</head>
<body>
    <theme:zone name="details">
       	<div class="row-fluid">
			<f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="timeCreated"/>
        			<f:ref property="createdBy" textProperty="username"/>
					<f:ref property="compartment" textProperty="label"/>
					<f:ref property="simulationConfig" textProperty="name"/>
    			</div>
    			<div class="span5">
					<f:ref property="evaluation" textProperty="name"/>
					<f:ref property="actionSeries" textProperty="name"/>
					<f:display property="repetitions"/>
					<f:display property="length"/>
    			</div>
			</f:with>
		</div>

        <hr>

		<div id="performanceChart" style="float:center">
      	</div>

    </theme:zone>
</body>
</html>