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
					return 'net_damage_spreading_' + ${ids.get(ids.size - 1)} + '-' + ${ids.get(0)} + '.csv'
		    	}					    		
			});

			$("#damageSpreadingChartDrawAll").prop('checked', true);
			$("#damageSpreadingChart").chart("populate", ${chartData});
		});

	</r:script>
</head>
<body>
    <theme:zone name="details">
    	<ui:field label="Ids">
			<ui:fieldInput>${ids}</ui:fieldInput>
		</ui:field>
        <hr>
		<div id="damageSpreadingChart" style="float:center">
      	</div>
    </theme:zone>
</body>
</html>