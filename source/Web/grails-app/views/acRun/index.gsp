<html>
    <head>
        <theme:title>Launch Chemistry</theme:title>
        <r:require module="chart"/>
        <r:require module="timer"/>
        <r:require module="select"/>
        <theme:layout name="main"/>
        <r:script>

			google.load("visualization", "1", {packages:["controls"]});

			$(document).ready(function(){
				$('#compartmentId').attr("data-none-selected-text", "---");
    			$('#compartmentId').selectpicker({
					dropupAuto: false
				});
    			$('#simulationConfigId').selectpicker({
					dropupAuto: false
				});
   				$('#interactionSeriesId').selectpicker({
					dropupAuto: false
				});
	   			$('#translationSeriesId').selectpicker({
					dropupAuto: false
				});

				$("#toggleSidebar").on('click', function () {
					$("#chart1").chart("redraw");
					$("#chart2").chart("redraw");
				});

 				$(window).resize(function(){
        			$("#chart1").chart("redraw");
        			$("#chart2").chart("redraw");
    			});

				$("#chart1Panel").hide()
				$("#chart2Panel").hide()

				$('#compartmentId').focus();

                $.getJSON('${createLink(action: "getCompartmentDependentData")}?id=' + $("#compartmentId").val(),
                    function(data) {
                        updateCombosWithData(data);
                        console.log(${instance.interactionSeries?.id})
					    $("#interactionSeriesId").val(${instance.interactionSeries?.id})
				        $("#translationSeriesId").val(${translationSeriesId})
				        $('#interactionSeriesId').selectpicker('refresh');
						$('#translationSeriesId').selectpicker('refresh');
					    if (${runnow}) {
				    		$("#runSimulationForm").submit();
			    		}
                    });
			});

			var chartToken;

			var timer = $.timer(function() {
				$.post('${createLink(controller: "acRun", action: "checkRunResponse")}',
					{'token' : chartToken},
					function callback(datas) {
						timer.stop();
						drawChart(datas)
					}
				).fail(function(xhr) {
				    if (xhr.status == 404) {
				        // need to wait
				    } else {
				        timer.stop();
				        showErrorMessage("Chemistry simulation failed due to: " + xhr.responseText);
				    }
  				})
			});

      		function getToken(data) {
      			if (data.errors != undefined) {
      				showErrors(data.errors);
      				return;
      			}
      			hideErrors();
				showCompartmentRunLaunchSuccessDialog();      		

      			chartToken = data;
      			timer.set({ time : 500});
	    		timer.play();
      		}

      		function drawChart(datas) {
				$("#collapseOne").collapse('hide')

				if (datas[0].title == "Chemistry") {
					$("#chart1Label").html('Concentration Chart');				
					$("#chart1").chart( {
		            	fileNameFun : function() {
							return 'co_' + $('#compartmentId').val() + '_as_' + $('#interactionSeriesId').val() + '_t_' + $('#runTime').val() + '.csv'
		            	}
					});
					$("#chart1DrawAll").prop('checked', false);
					$("#chart1PointsSlider").slider('value', 200);
					$("#chart1Points").val(200);

					$("#chart2Label").html('Translation Chart');
					$("#chart2").chart( {
						fileNameFun : function() {
							return 'co_' + $('#compartmentId').val() + '_as_' + $('#interactionSeriesId').val() + '_t_' + $('#runTime').val() + '_ts_' + $('#translationSeriesId').val() + '.csv'
				        }
					});
					$("#chart2DrawAll").prop('checked', true);
					$("#chart2PointsSlider").slider('value', 200);
					$("#chart2Points").val(200);
				} else {
					$("#chart1Label").html('Translation Chart');
					$("#chart1").chart( {
						fileNameFun : function() {
							return 'co_' + $('#compartmentId').val() + '_as_' + $('#interactionSeriesId').val() + '_t_' + $('#runTime').val() + '_ts_' + $('#translationSeriesId').val() + '.csv'
				        }
					});
					$("#chart1DrawAll").prop('checked', true);
					$("#chart1PointsSlider").slider('value', 200);
					$("#chart1Points").val(200);
				}

				$("#chart1").chart("populate", datas[0]);
				$("#chart1Panel").show()
				$("#collapseTwo").collapse('show')

				if (datas.length > 1) {
					$("#chart2Panel").show()
					$("#collapseThree").collapse('show')
					$("#chart2").chart("populate", datas[1]);
				} else
					$("#chart2Panel").hide()

				if ($("#translationForwardUrl").val()) {
					var url = $("#translationForwardUrl").val()
					var separator = $("#translationForwardSeparator").val();

					if (datas[0].title == "Translated Chemistry")
						window.open(url + datas[0].series[0].join(separator))
					else if (datas[1].title == "Translated Chemistry")
						window.open(url + datas[1].series[0].join(separator))
				}
      		};

  			function updateCombos() {
				$.getJSON('${createLink(action: "getCompartmentDependentData")}?id=' + $("#compartmentId").val(), updateCombosWithData);
  			};

            function updateCombosWithData(data) {
				$("#interactionSeriesId").populateAndSortKeyValues(data.actionSeries, false, true);
				$("#translationSeriesId").populateAndSortKeyValues(data.translationSeries, true, true);
				$('#interactionSeriesId').selectpicker('refresh');
				$('#translationSeriesId').selectpicker('refresh');
                if (Object.keys(data.actionSeries).length == 0) {
				    $('#add-interaction-series-modal').find("#speciesSet\\.id").val(data.speciesSetId);
					$('#add-interaction-series-modal').modal({ keyboard: true });
			    }
            }

			function showCompartment() {
				if ($("#compartmentId").val())
					window.open('${createLink(controller: "acCompartment", action: "show")}?id=' + $("#compartmentId").val())
			}

			function showSimulationConfig() {
				if ($("#simulationConfigId").val())
					window.open('${createLink(controller: "acSimulationConfig", action: "show")}?id=' + $("#simulationConfigId").val())
			}

			function showInteractionSeries() {
				if ($("#interactionSeriesId").val())
					window.open('${createLink(controller: "acInteractionSeries", action: "show")}?id=' + $("#interactionSeriesId").val())
			}

			function showTranslationSeries() {
				if ($("#translationSeriesId").val())
					window.open('${createLink(controller: "acTranslationSeries", action: "show")}?id=' + $("#translationSeriesId").val())
			}

			function showCompartmentRunLaunchSuccessDialog() {
				$('#launch-success-modal').modal({ keyboard: true });
			}

			function translationSeriesUpdated() {
				if ($("#translationSeriesId").val())
					$("#translationSeriesOptionalDiv").show();
				else	
					$("#translationSeriesOptionalDiv").hide();
			}
		</r:script>
    </head>
    <body>
        <theme:zone name="body">
			<gui:modal id="launch-success-modal" title="Chemistry Launch Success">
      			<p>Chemistry simulation has been successfully launched.</p>
      			<p>The result will appear in a while.</p>
			</gui:modal>

            <gui:modal id="add-interaction-series-modal" title="Interaction Series Needed" controller="acInteractionSeries" action="create">
                <g:hiddenField name="speciesSet.id" value=""/>
                <p>No interaction series found for the selected compartment.</p>
                <p>Do you want to create one?</p>
            </gui:modal>

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
	            			<ui:form remote="true" name="runSimulationForm" url="[controller: 'acRun', action: 'runSimulationAsync']" onSuccess="getToken(data);">
	            				<ui:field label="Compartment">
	            					<ui:fieldInput>
	            						<g:select name="compartmentId" class="span6" from="${compartments}" optionKey="id"
	            							optionValue="${{it.id + ' : ' + it.label}}"
	            							value="${instance.compartment?.id}"
	            							data-header="Select One"
	            							data-live-search="true"
	            							onchange="updateCombos();"/>
	            						<a href="javascript:void(0);" onclick="showCompartment();">
	            							<i class="icon-eye-open"></i>
	            						</a>
                        			</ui:fieldInput>
                        		</ui:field>
                        		<ui:field label="Interaction Series">
                        			<ui:fieldInput>
        	    	    				<g:select name="interactionSeriesId" class="span6" from=""
        	    	    					optionValue="${{it.id + ' : ' + it.name}}"
        	    	    					data-live-search="true"/>
	            						<a href="javascript:void(0);" onclick="showInteractionSeries();">
	            							<i class="icon-eye-open"></i>
	            						</a>
        	    	    			</ui:fieldInput>
                        		</ui:field>
                        		<ui:field label="Run Time" class="span2">
                        			<ui:fieldInput>
                        				<g:textField name="runTime" value="${instance.runTime}"/>
        	    	   				</ui:fieldInput>
                       			</ui:field>
	            				<ui:field label="Simulation Config">
	            					<ui:fieldInput>
	            						<g:select name="simulationConfigId" class="span6" from="${simulationConfigs}" optionKey="id"
	            							optionValue="${{it.id + ' : ' + it.name}}"
	            							value="${instance.simulationConfig?.id}"
	            							data-header="Select One"
	            							data-live-search="true"/>
	            						<a href="javascript:void(0);" onclick="showSimulationConfig();">
	            							<i class="icon-eye-open"></i>
	            						</a>
                        			</ui:fieldInput>
                        		</ui:field>
            		    		<ui:field label="Translation Series">
                        			<ui:fieldInput>
        	    	    				<g:select name="translationSeriesId" class="span6" from=""
        	    	    					optionValue="${{it.id + ' : ' + it.name}}"
											data-live-search="true"
        	    	    					onchange="translationSeriesUpdated();"/>
	            						<a href="javascript:void(0);" onclick="showTranslationSeries();">
	            							<i class="icon-eye-open"></i>
	            						</a>
        	    	    			</ui:fieldInput>
                        		</ui:field>
                        		<div id="translationSeriesOptionalDiv" style="display:none">
                        			<ui:field label="Show Conc. Traces">
                        				<ui:fieldInput>
                        					<g:checkBox name="storeRunTrace" checked="${true}"/>
        	    	    				</ui:fieldInput>
                        			</ui:field>

                        			<ui:field label="Translation Forward URL">
                        				<ui:fieldInput>
                        					<g:textField name="translationForwardUrl" value=""/>
        	    	    				</ui:fieldInput>
                        			</ui:field>

                        			<ui:field label="Translation Forward Separator">
                        				<ui:fieldInput>
                        					<g:textField class="span1" name="translationForwardSeparator" value=""/>
        	    	    				</ui:fieldInput>
                        			</ui:field>
                        		</div>
       							<ui:actions>
       					    		<ui:button type="submit" kind="button" mode="primary">Run</ui:button>
       								<ui:button kind="anchor">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
            					</ui:actions>
            				</ui:form>
						</div>
      				</div>
    			</div>

  				<div id="chart1Panel" class="accordion-group">
    				<div class="accordion-heading">
        				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
							<div id="chart1Label">
        						Concentration Chart
        					</div>
        				</a>
    				</div>
    				<div id="collapseTwo" class="accordion-body collapse">
      					<div class="accordion-inner">
							<div id="chart1" style="float:center"></div>
      					</div>
      				</div>
    			</div>

  				<div id="chart2Panel"class="accordion-group">
    				<div class="accordion-heading">
        				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
							<div id="chart2Label">
        						Translation Chart
        					</div>
        				</a>
    				</div>
    				<div id="collapseThree" class="accordion-body collapse">
      					<div class="accordion-inner">
							<div id="chart2" style="float:center"></div>
      					</div>
      				</div>
    			</div>
    		</div>
        </theme:zone>
    </body>
</html>