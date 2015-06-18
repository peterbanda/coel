<html>
<head>
	<theme:title>Run Network</theme:title>
	<r:require module="jquery-ui"/>
	<r:require module="timer"/>
	<r:require module="jszip"/>
	<theme:layout name="main"/>
	<r:script>
		var images;
		var imageFormat;
		var imageWidth;
		var imageHeight;

		var imageIndex = 0;
		var indexInc = 1;
		var imageSize = 200;

		$(document).ready(function(){
			$('#networkId').focus();
			$("#networkRunChartPanel").hide();
			$('#forwardButton').hide();

			$( "#networkRunLaunchSuccess-Dialog" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					OK: function() {
						$( this ).dialog( "close" );
					}
				}
			});

	    	$("#sizeSlider").slider({
				min: 100,
				max: 500,
	    	   	value: 200,
	    	   	slide: function( event, ui ) {
	    	   		timer.stop()
	    	   		$("#size").val(ui.value)
	    	   	},
	    	   	stop: function( event, ui ) {
	    	   		imageSize = ui.value
	    	   		$('#networkRunChartDiv').attr('style', "height:" + (imageSize + 75) + "px");
	    	   		timer.play()
	    		}
	    	});

	    	$("#delaySlider").slider({
				min: 25,
				max: 500,
	    	   	value: 100,
	    	   	slide: function( event, ui ) {
	    	   		timer.stop()
	    	   		$("#delay").val(ui.value)
	    	   	},
	    	   	stop: function( event, ui ) {
	    	   		timer.set({ time : ui.value});
	    	   		timer.play()
	    		}
	    	});

			timer.set({ time : 100, autostart : false });
			$("#delay").val(100)
			$("#size").val(200)
			$("#topologySizesDiv").hide()
		});

		var timer = $.timer(function() {
			if (images) {
				if (imageIndex >= images.length)
					imageIndex = 0;
				else if (imageIndex < 0)
					imageIndex = images.length - 1;
				$("#frameNum").val(imageIndex)
				if (imageFormat == "svg")
					$("#networkRunChart").html('<object data="data:image/svg+xml;base64,' + images[imageIndex] + '" width="' + imageSize + '" height="' + imageSize + '"></object>');
				else
      				$("#networkRunChart").html('<object data="data:image/' + imageFormat + ';base64,' + images[imageIndex] + '" width="' + imageSize + '" height="' + imageSize + '"></object>');

      			imageIndex += indexInc;
			}
		});

  		function updateTopologySize() {
			$.getJSON('${createLink(action: "getTopologySizes")}?id=' + $("#networkId").val(),
				function(data) {
					if (data) {
						$("#topologySizes").val(data);
						$("#topologySizesDiv").show();
					} else {
						$("#topologySizes").val("");
						$("#topologySizesDiv").hide();
					}
			});
  		};

      	function drawChart(data) {
      		timer.stop()
      		$("#networkRunChartPanel").show()
			$("#collapseOne").collapse('hide')
			$("#collapseTwo").collapse('show')

			imageIndex = 0
			images = data.images
			imageFormat = data.format
			imageWidth = data.width
			imageHeight = data.height
			$("#exportFileButton").html('<i class="icon-file"></i>' + imageFormat)
          	timer.play()
      	};

		function showNetworkRunLaunchSuccessDialog() {
			$( "#networkRunLaunchSuccess-Dialog" ).dialog( "open" );
		}

		function changeToBackwardDirection() {
			$('#backwardButton').hide();
			$('#forwardButton').show();
			indexInc = -1;
			timer.play();
		}

		function changeToForwardDirection() {
			$('#forwardButton').hide();
			$('#backwardButton').show();
			indexInc = 1;
			timer.play();
		}

		function exportAllAsZip() {
			var zip = new JSZip();
			zip.file("info.txt", "Exported By COEL " + currentDateTimeAsString() + "\n------------------------------------\nNetwork : " + $('#networkId').val() + "\nOne-Zero Ratio : " + $('#oneZeroRatio').val() + "\nRun time : " + $('#runTime').val());
			var img = zip.folder("images");
			$.each(images, function(i, image) {
				img.file(i + "." + imageFormat, image, {base64: true});
	      	});
			var content = zip.generate();
			saveAs('data:application/zip;base64,' + content, 'network_run_' + $('#networkId').val() + "_" + $('#runTime').val() + '.zip')
		}

		function exportCurrent() {
			var filename = 'network_run_' + $('#networkId').val() + "_" + imageIndex + "-" + $('#runTime').val() + '.' + imageFormat
			if (imageFormat == "svg")
				saveAs('data:image/svg+xml;base64,' + images[imageIndex], filename);
			else
				saveAs('data:image/' +  imageFormat + ';base64,' + images[imageIndex], filename)
		}

	</r:script>
</head>
<body>
	<theme:zone name="body">
        <div id="networkRunLaunchSuccess-Dialog" title="Network Run">
         	<p>Network simulation has been successfully launched.</p>
		</div>
		<div id="errorDiv" >
			<g:eachError bean="${instance}" var="error">
				<ui:message type="error"><g:message error="${error}"/></ui:message>
			</g:eachError>
		</div>
		<div class="accordion" id="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Specification</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
		            	<ui:form remote="true" name="runSimulationForm" url="[controller: 'networkRun', action: 'runSimulation']" before="showNetworkRunLaunchSuccessDialog();" onSuccess="drawChart(data);">
	    	        		<ui:field label="Network">
	        	    			<ui:fieldInput>
									<g:select name="networkId" from="${networks}"
										optionKey="id"
                        	            optionValue="${{it.id + ' : ' + it.name}}"
                        				value="${networkRunTaskInstance?.network?.id}"
    									noSelection= "['': 'Select One...']"
    									onchange="updateTopologySize();"/>
                        		</ui:fieldInput>
                        	</ui:field>
                        	<div id="topologySizesDiv">
                        		<ui:field label="Topology Sizes">
                        			<ui:fieldInput>
                        				<g:textField name="topologySizes" value=""/>
                        			</ui:fieldInput>
                        		</ui:field>
                        	</div>
            		    	<ui:field bean="instance" name="runTime"/>
							<ui:field label="One-Zero Probability" name="oneZeroRatio" value="0.5"/>
							<hr>
							<ui:field label="Format">
								<ui:fieldInput>
									<g:select class="span1" name="format" from="${["gif", "png", "jpg", "svg"]}"/>
								</ui:fieldInput>
							</ui:field>
       						<ui:actions>
	       					    <ui:button type="submit" kind="button" mode="primary">Run</ui:button>
    	   						<ui:button kind="anchor">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
        	    			</ui:actions>
            			</ui:form>
					</div>
      			</div>
    		</div>
  			<div id="networkRunChartPanel" class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">Network Chart</a>
    			</div>
    			<div id="collapseTwo" class="accordion-body collapse">
      				<div id="networkRunChartDiv" class="accordion-inner" style="height:250px">
      					<div class="span7 pagination-centered">
      						<div class="row-fluid" style="margin: 20px 0px 10px 0px;">
      					  		<div class="span1 offset5">
      					  			<label for="frameNum">Frame:</label>
      					  		</div>
								<div class="span1">
  			  						<input id="frameNum" style="width:35px; font-weight: bold;" value="" readonly="readonly" disabled="true"/>
  			  					</div>
							</div>
      						<div class="row-fluid" id="networkRunChart"></div>
      					</div>
      					<div class="span3" style="margin: 35px 0px 10px 0px;">
							<ui:block>
      						<table>
      							<tr>
      								<td>	
      									<label for="size">Size:</label>
      								</td>
      								<td>
      									<input id="size" style="width:35px; font-weight: bold;" value="" readonly="readonly" disabled="true"/>
      								</td>
      							</tr>
      						</table>
      						<div id="sizeSlider"></div>
      						<table style="margin: 10px 0px 10px 0px;">
      							<tr>
      								<td>	
      									<label for="delay">Delay:</label>
      								</td>
      								<td>
      									<input id="delay" style="width:35px; font-weight: bold;" value="" readonly="readonly" disabled="true"/>
      								</td>
      							</tr>
      						</table>
  			  				<div id="delaySlider"></div>
  			  				<div class="row-fluid pagination-centered" style="margin: 10px 0px 10px 0px;">
								<ui:button id="playPauseButton" kind="button" onclick="timer.toggle();return false;">
									<i class="icon-play"></i>
									<i class="icon-pause"></i>
								</ui:button>
								<ui:button id="backwardButton" kind="button" onclick="changeToBackwardDirection();return false;">
									<i class="icon-backward"></i>
								</ui:button>
								<ui:button id="forwardButton" kind="button" onclick="changeToForwardDirection();return false;">
									<i class="icon-forward"></i>
								</ui:button>
							</div>
							</ui:block>
							<ui:block>
								<div class="pagination-centered">
									<ui:button id="exportFileButton" kind="button" onclick="exportCurrent();return false;">
										<i class="icon-file"></i>
									</ui:button>
									<ui:button kind="button" onclick="exportAllAsZip();return false;">
										<i class="icon-file"></i>zip (all)
									</ui:button>
								</div>
							</ui:block>
  			  			</div>
  			  		</div>
    			</div>
			</div>
		</div>
	</theme:zone>
</body>
</html>