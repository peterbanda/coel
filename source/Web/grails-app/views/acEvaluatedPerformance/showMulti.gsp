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
					return 'performance_' + ${ids.get(ids.size - 1)} + '-' + ${ids.get(0)} + '.csv'
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
			<ui:fieldInput>
				<div class="spacedTop">
				<ul class="inline">
					<g:each var="id" in="${ids}">
						<li>
							<g:link action="show" id="${id}">
								${id}
							</g:link>
						</li>
					</g:each>
				</ul>
				</div>
			</ui:fieldInput>
		</ui:field>
        <hr>
		<div id="performanceChart" style="float:center">
      	</div>
    </theme:zone>
</body>
</html>