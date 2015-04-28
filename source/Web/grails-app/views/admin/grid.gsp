<html>
<head>
	<theme:title>Grid Stats</theme:title>
	<r:require module="timer"/>
	<r:require module="moment"/>
	<theme:layout name="main"/>
	<nav:set path="app"/>
	<r:script>
		var timer = $.timer(updateContent);

		function updateContent() {
			$.getJSON('${createLink(action: "getGridMetrics")}',
				function(metrics) {
					$('#totalActiveJobs').html(metrics.totalActiveJobs)
     				$('#totalCpus').html(metrics.totalCpus)
     				$('#totalNodes').html(metrics.totalNodes)
		 			$('#averageActiveJobs').html(+metrics.averageActiveJobs.toFixed(3))
		 			$('#averageCpuLoad').html(+metrics.averageCpuLoad.toFixed(3))
		 			$('#averageRejectedJobs').html(+metrics.averageRejectedJobs.toFixed(3))
					$('#averageIdleTime').html(getDurationAsString(metrics.averageIdleTime))
					$('#averageJobExecuteTime').html(getDurationAsString(metrics.averageJobExecuteTime))
					$('#averageJobWaitTime').html(getDurationAsString(metrics.averageJobWaitTime))
					$('#averageUpTime').html(getDurationAsString(metrics.averageUpTime))
					$('#minimumUpTime').html(getDurationAsString(metrics.minimumUpTime))
				});
		}

		function getDurationAsString(ms) {
			var dur = moment.duration(parseInt(ms, 10))
			var time = {
              	years : dur.years(),
              	months : dur.months(),
              	days : dur.days(),
              	hours : dur.hours(),
              	mins : dur.minutes(),
            	secs : dur.seconds()
			};

			var output = time.secs 
			if (time.mins == 0)
				return output

			if (time.secs < 10)
				output = "0" + output

			output = time.mins + ":" + output

			if (time.hours == 0)
				return output

			if (time.mins < 10)
				output = "0" + output

			output = time.hours + ":" + output

			if (time.days == 0)
				return output

			if (time.days == 1)
				output = time.days + " day " + output
			else
				output = time.days + " days " + output

			if (time.months == 0)
				return output

			if (time.months == 1)
				output = time.months + " month, " + output
			else
				output = time.months + " months, " + output

			if (time.years == 0)
				return output

			if (time.years == 1)
				output = time.years + " year, " + output
			else
				output = time.years + " years, " + output

			return output
		}

		$(document).ready(function(){
			timer.set({ time : 3000, autostart : true });
			updateContent();
		});

	</r:script>
</head>
<body>
    <theme:zone name="body">
    	<div class="form-horizontal form-dense">
    	  	<ui:form remote="true" name="taskCancelForm" url="[controller: 'admin', action: 'cancelTask']" onSuccess="showMessage(data);">
	          	<ui:field label="Task Id">
	            	<ui:fieldInput>
	            		<g:textField name="taskId" class="span6" value=""/>
	            		<ui:button type="submit" kind="button" mode="primary">Cancel Task</ui:button>
           			</ui:fieldInput>
           		</ui:field>
    		</ui:form>
    		<hr/>
    	    <f:with bean="metrics">    		
    	        <f:display property="totalNodes"/>
     			<f:display property="totalCpus"/>
				<hr/>
				<f:display property="totalActiveJobs"/>
	 			<f:display property="averageActiveJobs"/>
	 			<f:display property="averageCpuLoad"/>
	 			<f:display property="averageRejectedJobs"/>
				<hr/>
				<f:display property="averageIdleTime"/>
				<f:display property="averageJobExecuteTime"/>
				<f:display property="averageJobWaitTime"/>
				<f:display property="averageUpTime"/>
				<f:display property="minimumUpTime"/>
     		</f:with>
     	</div>
	</theme:zone>
</body>
</html>