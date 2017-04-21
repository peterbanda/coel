<html>
<head>
	<theme:title>CA Simulation on a Shift-Symmetric Configuration</theme:title>
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
		var imageSize = 300;

		$(document).ready(function(){
			$('#networkId').focus();
			$("#networkRunChartPanel").hide();
			$('#forwardButton').hide();

	    	$("#sizeSlider").slider({
				min: 100,
				max: 500,
	    	   	value: 300,
	    	   	slide: function( event, ui ) {
	    	   		timer.stop()
	    	   		$("#size").val(ui.value)
	    	   	},
	    	   	stop: function( event, ui ) {
	    	   		imageSize = ui.value
	    	   		$('#networkRunChartDiv').attr('style', "height:" + Math.max(imageSize + 75, 320) + "px");
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
			$("#size").val(300)
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

      	function drawChart(data) {
            $("body").css("cursor", "default");
            if (data.errors) {
                showErrors(data.errors);
            } else {
      		    hideErrors();
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
            }
      	};

        function simStart() {
            timer.stop();
            $("body").css("cursor", "wait");
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
			var latticeSize = $('#latticeSize').val()
			saveAs('data:application/zip;base64,' + content, '2dca_run_' + latticeSize + "x" + latticeSize + "_" + $('#runTime').val() + '.zip')
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
		<div id="errorDiv">
		</div>
		<div class="accordion" id="accordion">
			<div class="accordion-group">
				<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">Specification</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
		            	<ui:form remote="true" name="runSimulationForm" url="[action: 'runSimulation']" before="simStart();" onSuccess="drawChart(data);">
							<ui:field label="Lattice Size (^2)" name="latticeSize" value="40"/>
							<ui:field label="Symmetry Shift X" name="symmetryShiftX" value="10"/>
                            <ui:field label="Symmetry Shift Y" name="symmetryShiftY" value="5"/>
							<ui:field label="Run Time" name="runTime" value="100"/>
							<hr>
							<ui:field label="Format">
								<ui:fieldInput>
									<g:select class="span1" name="format" from="${["gif", "png", "jpg", "svg"]}" value="gif"/>
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
      				<div id="networkRunChartDiv" class="accordion-inner" style="height:375px">
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