<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkDamageSpreading.label', default: 'Network Damage Spreading')}" scope="request" />
    <nav:set path="app/network/DamageSpreading"/>
    <r:require module="chart"/>
    <r:require module="jquery-ui"/> 
    <theme:layout name="show"/>
    <r:script>
		google.load("visualization", "1", {packages:["controls"]});

		$(document).ready(function(){

			$("#damageSpreadingChart").chart( {
		    	fileNameFun : function() {
					return 'net_damage_spreading_' + ${instance.id} + '.csv'
		        }					    		
			});

			$("#damageSpreadingChartDrawAll").prop('checked', true);
			$("#damageSpreadingChart").chart("populate", ${chartData});
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

		<div id="damageSpreadingChart" style="float:center">
      	</div>

    </theme:zone>
</body>
</html>