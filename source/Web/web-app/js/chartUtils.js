		        $.widget("custom.chart", {

		        	// the constructor
			        _create: function() {

			        	var elementId = this.element.attr('id')

		        		this.seriesSelectionId = elementId + 'SeriesSelection'
		        		this.pointsSliderId = elementId + 'PointsSlider'
		        		this.pointsId = elementId + 'Points'
		        		this.drawAllId = elementId + 'DrawAll'
		        		this.exportLinkId = elementId + 'ExportLink'
		        		this.fullExportLinkId = elementId + 'FullExportLink'
		        		this.chartDivId = elementId + 'ChartDiv'
		        		this.rangeDivId = elementId + 'RangeDiv'
		        		this.rangeFromId = elementId + 'RangeFrom'
		        		this.rangeToId = elementId + 'RangeTo'

	    	   			this.element.html('<div class="row-fluid">\
	    	   							<div class="span9">\
	    	   								<div class="row-fluid" id="' + this.chartDivId + '" style="width:100%; height:400px"></div>\
	            	  			    		<div class="row-fluid">\
	    	   									<div class="span2">\
	      					  						<input class="span10 offset2" id="' + this.rangeFromId + '" style="font-weight: bold;" value=""/>\
	      					  					</div>\
	      					  					<div class="span8">\
	      					  						<div id="' + this.rangeDivId + '"></div>\
	      					  					</div>\
	      					  					<div class="span2">\
	      					  						<input class="span10" id="' + this.rangeToId + '" style="font-weight: bold;" value=""/>\
	      					  					</div>\
	      					  				</div>\
		            	  				</div>\
		            	  				<div class="span3">\
	      					  				<div class="alert alert-info">\
	      					  					<div class="row-fluid">\
  					  								<div class="span4">\
  			  											<label for="' + this.pointsId + '"># points:</label>\
  			  										</div>\
  			  										<div class="span3">\
  			  											<input id="' + this.pointsId + '" style="width:35px; font-weight: bold;" value="" readonly="readonly" disabled="true"/>\
  			  										</div>\
  			  										<div class="span3">\
  			  											<label for="' + this.drawAllId + '">or All</label>\
  			  										</div>\
  			  										<div class="span2">\
  			  											<input type="checkbox" id="' + this.drawAllId + '"/>\
  			  										</div>\
  			  									</div>\
  			  									<div id="' + this.pointsSliderId + '" class="row-fluid"/>\
  			  								</div>\
											<div class="row-fluid">\
												<select id="' + this.seriesSelectionId + '" name="' + this.seriesSelectionId + '" multiple="multiple">\
												</select>\
											</div>\
											<div class="row-fluid">\
												<a id="' + this.exportLinkId + '" href="javascript:void(0);" class="btn btn-info">Export Visible</a>\
												<a id="' + this.fullExportLinkId + '" href="javascript:void(0);" class="btn btn-info">Export Raw</a>\
											</div>\
										</div>\
		      			  			</div>');

			        	var that = this;

			        	$('#' + this.rangeFromId).change(function() {
			        		$('#' + that.rangeDivId).slider( "values", [ $('#' + that.rangeFromId).val(), $('#' + that.rangeToId).val() ] );
			        		that._draw(that);
			        	});

			        	$('#' + this.rangeToId).change(function() {
			        		$('#' + that.rangeDivId).slider( "values", [ $('#' + that.rangeFromId).val(), $('#' + that.rangeToId).val() ] );
			        		that._draw(that);
			        	});

		        		$('#' + this.exportLinkId).click(function() {
		      				var selectedColumnStrings = $("#" + that.seriesSelectionId).val() || ['0'];
		      				exportChartAsCSV(that.chart, selectedColumnStrings, that.options.fileNameFun())
		        		});

		        		$('#' + this.fullExportLinkId).click(function() {
		      				var selectedColumnStrings = $("#" + that.seriesSelectionId).val() || ['0'];
		      				exportFullChartDataAsCSV(that.chartData, $('#' + that.rangeFromId).val(), $('#' + that.rangeToId).val(), selectedColumnStrings, that.options.fileNameFun())
		        		});

		        		$('#' + this.drawAllId).click(function() {
		        		    var $this = $(this);   
		        		    if ($this.is(':checked')) {
		        		    	if (confirm('If the number of points is too high it might break your browser.\nWould you like to proceed?')) {
		        		    		$("#" + that.pointsSliderId).slider("disable");
		        		    		that._draw(that)
		        		    	} else {
		        		    		$this.prop('checked', false);
		        		    	}
		        		    } else {
		        		    	$("#" + that.pointsSliderId).slider("enable");
		        		    	that._draw(that)
		        		    }
		        		});

	    	   			$("#" + this.seriesSelectionId).multiselect({
	       					selectedText: "# series selected",
	       					noneSelectedText: 'Filter series',
	       					height: "auto",
	       					minWidth: "140",
	       					close: function(event, ui){
	       						that._draw(that);
	       		            }
	    	   			});

	    	   			$("#" + this.pointsSliderId).slider({
	    	   				min: 50,
	    	   				max: 2000,
	    	   				value: 200,
	    	   				slide: function( event, ui ) {
	    	   					$("#" + that.pointsId).val( ui.value );
	    	   				},
	    	   				stop: function( event, ui ) {
	    	   					that._draw(that);
	    		   			}
	    	   			});

	    	   			$("#" + this.pointsId).val( $("#" + this.pointsSliderId).slider( "value" ) );
	        		},

	        		populate : function(_chartData) {
	        			this.chartData = _chartData
						var xSteps = this.chartData.xSteps;

	        			var drawThis = this._draw.curry(this);

		        		this.chart = createChart(this.chartData.xAxisCaption, this.chartData.yAxisCaption, this.chartData.title, this.chartDivId);
		        		createSlider(xSteps[0], xSteps[xSteps.length - 1], this.rangeDivId, this.rangeFromId, this.rangeToId, drawThis);
						$("#" + this.seriesSelectionId).populate(this.chartData.seriesGroups);
						drawThis();
	        		},

	        		_draw : function(that) {
	        			var xDisplayFrom = $("#" + that.rangeFromId).val();
	        			var xDisplayTo = $("#" + that.rangeToId).val();
	      				var selectedSeries = $("#" + that.seriesSelectionId).val() || ['0'];

	        			if ($("#" + that.drawAllId).is(':checked')) {
	        				drawChartFull(that.chart, that.chartData, xDisplayFrom, xDisplayTo, selectedSeries);
	        			} else {
	        				var points = $("#" + that.pointsSliderId).slider( "value" );
		    	      		drawChartSmooth(that.chart, that.chartData, xDisplayFrom, xDisplayTo, selectedSeries, points);
	        			}
	    	      	},

	        		redraw : function() {
	        			var drawThis = this._draw.curry(this);
	        			drawThis();
	    	      	}
	        	});


			Function.prototype.curry = function() {
    	    	var fn = this, args = Array.prototype.slice.call(arguments);
        		return function() {
        			return fn.apply(this, args.concat(Array.prototype.slice.call(arguments)));
        		};
        	};

			function exportChartAsCSV(chart, selectedColumnStrings, fileName) {
  				var selectedColumns = [];
  				selectedColumns.push(0);
            	$.each(selectedColumnStrings, function(i, string) {
            		selectedColumns.push(parseInt(string) + 2);
    			});
				var table = dataTableToCSV(chart.getDataTable(), selectedColumns);
				saveCsvAs(table, fileName);
            }

			function exportFullChartDataAsCSV(chartData, xDisplayFrom, xDisplayTo, selectedColumnStrings, fileName) {
				var labels = getLabels(chartData)

  				var selectedColumns = [];
  				selectedColumns.push(0);
            	$.each(selectedColumnStrings, function(i, string) {
            		selectedColumns.push(parseInt(string) + 2);
    			});

            	var dataTable = createDataTableFull(chartData.xAxisCaption, chartData.xSteps, xDisplayFrom, xDisplayTo, labels, chartData.series, selectedColumnStrings);
				var table = dataTableToCSV(dataTable, selectedColumns);
				saveCsvAs(table, fileName);
            }

            function saveCsvAs(table, fileName) {
            	saveAs('data:application/csv;charset=utf-8,' + encodeURIComponent(table), fileName)
            }

            function dataTableToCSV(dataTable, selectedColumns) {
            	var dt_cols = dataTable.getNumberOfColumns();
            	var dt_rows = dataTable.getNumberOfRows();
            	var csv_cols = [];
            	var csv_out;

            	// Iterate columns
            	$.each(selectedColumns, function(i, selectedColumn) {
    	        	csv_cols.push(dataTable.getColumnLabel(selectedColumn).replace(/,/g,""));
    			});

            	csv_out = csv_cols.join(",") + "\r\n";

            	for (i = 0; i < dt_rows; i++) {
	            	var raw_col = [];
            		$.each(selectedColumns, function(j, selectedColumn) {
            	    	// Replace any commas in row values
                		raw_col.push(dataTable.getFormattedValue(i, selectedColumn, 'label').replace(/,/g,""));
                	});
	            	// Add row to CSV text
    	        	csv_out += raw_col.join(",") + "\r\n";
    			}

            	return csv_out;
            }

            function getLabels(chartData) {
				var labels = new Array();
				var index = 0;

	      		$.each(chartData.seriesGroups, function(i, group) {
					$.each(group.labels, function(j, label) {
		      			labels[index] = label;
						index++;
					});
	      		});
	      		return labels
            }

			function drawChartFull(chart, chartData, xDisplayFrom, xDisplayTo, selectedSeries) {
				var labels = getLabels(chartData)
      			var dataTable = createDataTableFull(chartData.xAxisCaption, chartData.xSteps, xDisplayFrom, xDisplayTo, labels, chartData.series, selectedSeries);
      			drawChartWithTable(chart, dataTable, selectedSeries)
	      	}

			function drawChartSmooth(chart, chartData, xDisplayFrom, xDisplayTo, selectedSeries, points) {
				var labels = getLabels(chartData)
      			var dataTable = createDataTableSmooth(chartData.xAxisCaption, chartData.xSteps, xDisplayFrom, xDisplayTo, points, labels, chartData.series, selectedSeries);
      			drawChartWithTable(chart, dataTable, selectedSeries)
	      	}

			function drawChartWithTable(chart, dataTable, selectedSeries) {
				var columnsToDisplay = new Array();
				columnsToDisplay[0] = 0;
	      		$.each(selectedSeries, function(i, seriesIndex) {
					columnsToDisplay[i + 1] = parseInt(seriesIndex) + 2;
	      		});

	      		chart.setView({'columns' : columnsToDisplay});

        		// Draw the dashboard
	      		chart.setDataTable(dataTable);
	      		chart.draw();
	      	}
			
			function createDataTableFull(xLabel, xSteps, xDisplayFrom, xDisplayTo, seriesLabels, series, selectedSeries) {
        		var dataTable = new google.visualization.DataTable();

				var xStepsNum = xSteps.length;
				var xMin = xSteps[0];
				var xMax = xSteps[xSteps.length - 1];

    			var seriesNum = seriesLabels.length;
        		if (xDisplayFrom == undefined) {
        			xDisplayFrom = xMin;
        		}
        		if (xDisplayTo == undefined) {
        			xDisplayTo = xMax;
        		}

				// add columns
				dataTable.addColumn('number', xLabel);
				dataTable.addColumn('number', 'Range');
    			$.each(seriesLabels, function(i, label) {
    				dataTable.addColumn('number', label);
    			});

				// add x axis and determine x display from/to indeces
				var xDisplayIndeces = new Array();
				var index = 0;

    			$.each(xSteps, function(i, xStep) {
    				if (xStep >= xDisplayFrom && xStep <= xDisplayTo) {
        				dataTable.addRows(1);
        				dataTable.setValue(index, 0, xStep);
        				dataTable.setValue(index, 1, xStep);
    					xDisplayIndeces[index] = i;
						index++;
    				}
    			});

				// add data
    			$.each(series, function(seriesIndex, oneSeries) {
	        		if ($.inArray("" + seriesIndex, selectedSeries) > -1) {
	        			$.each(xDisplayIndeces, function(index, xDisplayIndex) {
		    				dataTable.setValue(index, seriesIndex + 2, oneSeries[xDisplayIndex]);
    	    			});
    		      	};	
    			});
    			return(dataTable);
      		};

			function createDataTableSkipped(xLabel, xSteps, xDisplayFrom, xDisplayTo, xDisplayStepsNum, seriesLabels, series, selectedSeries) {
        		var dataTable = new google.visualization.DataTable();

				var xStepsNum = xSteps.length;
				var xMin = xSteps[0];
				var xMax = xSteps[xSteps.length - 1];
    			var seriesNum = seriesLabels.length;
        		if (xDisplayFrom == undefined) {
        			xDisplayFrom = xMin;
        		}
        		if (xDisplayTo == undefined) {
        			xDisplayTo = xMax;
        		}

				// add columns
				dataTable.addColumn('number', xLabel);
				dataTable.addColumn('number', 'Range');
    			$.each(seriesLabels, function(i, label) {
    				dataTable.addColumn('number', label);
    			});

				// add x axis and determine x display from/to indeces
				var xDisplayIndecesCount = 0;
    			$.each(xSteps, function(i, xStep) {
    				if (xStep >= xDisplayFrom && xStep <= xDisplayTo) {
    					xDisplayIndecesCount++;
    				}
    			});

    			var xStepToDisplay = parseInt(xDisplayIndecesCount / xDisplayStepsNum);
   				if (xStepToDisplay < 1) {
    				xStepToDisplay = 1;
	   			}		

				var xDisplayIndeces = new Array();
				var index;
				var counter = 0;
				var lastXDisplayIndex;
    			$.each(xSteps, function(i, xStep) {
    				if (xStep >= xDisplayFrom && xStep <= xDisplayTo) {
        				if (index == undefined) {
            				index = 0;
            			}
        				if (index % xStepToDisplay == 0) {
        					dataTable.addRows(1);
        					dataTable.setValue(counter, 0, xStep);
        					dataTable.setValue(counter, 1, xStep);
    						xDisplayIndeces[counter] = i;
    						counter++;
    					}
						index++;
						lastXDisplayIndex = i;
    				}
    			});

				// we want to alway include the last step
				if (xDisplayIndeces[counter - 1] != lastXDisplayIndex) {
					dataTable.addRows(1);
					dataTable.setValue(counter, 0, xSteps[lastXDisplayIndex]);
					dataTable.setValue(counter, 1, xSteps[lastXDisplayIndex]);
					xDisplayIndeces[counter] = lastXDisplayIndex;					
				}

				// add data
    			$.each(series, function(seriesIndex, oneSeries) {
	        		if ($.inArray("" + seriesIndex, selectedSeries) > -1) {
	        			$.each(xDisplayIndeces, function(index, xDisplayIndex) {
		    				dataTable.setValue(index, seriesIndex + 2, oneSeries[xDisplayIndex]);
    	    			});
    		      	};	
    			});
    			return(dataTable);
      		};

			function createDataTableSmooth(xLabel, xSteps, xDisplayFrom, xDisplayTo, xDisplayStepsNum, seriesLabels, series, selectedSeries) {
        		var dataTable = new google.visualization.DataTable();

				var xStepsNum = xSteps.length;
				var xMin = xSteps[0];
				var xMax = xSteps[xSteps.length - 1];
    			var seriesNum = seriesLabels.length;
        		if (xDisplayFrom == undefined) {
        			xDisplayFrom = xMin;
        		}
        		if (xDisplayTo == undefined) {
        			xDisplayTo = xMax;
        		}

				// add columns
				dataTable.addColumn('number', xLabel);
				dataTable.addColumn('number', 'Range');
    			$.each(seriesLabels, function(i, label) {
    				dataTable.addColumn('number', label);
    			});

				// add x axis and determine x display from/to indeces
				var xDisplayIndecesCount = 0;
				var xDisplayFromIndex;
				var xDisplayToIndex;
    			$.each(xSteps, function(i, xStep) {
    				if (xDisplayFromIndex == undefined && xStep >= xDisplayFrom) {
    					xDisplayFromIndex = i;
    				}
    				if (xDisplayToIndex == undefined && xStep > xDisplayTo) {
    					xDisplayToIndex = i - 1;
    				}
    			});
    			if (xDisplayToIndex == undefined) {
    				xDisplayToIndex = xSteps.length - 1; 
        		}
    			var xStepSize = (xSteps[xDisplayToIndex] - xSteps[xDisplayFromIndex]) / xDisplayStepsNum;

    			// x axis
				var xStepDisplay = xSteps[xDisplayFromIndex];
				var index = 0;
    			for (var i = xDisplayFromIndex + 1; i <= xDisplayToIndex; ) {
					if (xSteps[i] >= xStepDisplay) {
    					dataTable.addRows(1);
    					dataTable.setValue(index, 0, xStepDisplay);
    					dataTable.setValue(index, 1, xStepDisplay);
    					xStepDisplay += xStepSize;
						index++;
					} else {
						i++;
					}
    			}

    			// series
    			$.each(series, function(seriesIndex, oneSeries) {
	        		if ($.inArray("" + seriesIndex, selectedSeries) > -1) {
	    				xStepDisplay = xSteps[xDisplayFromIndex];
	    				index = 0;
	        			for (var i = xDisplayFromIndex + 1; i <= xDisplayToIndex; ) {
        					if (xSteps[i] >= xStepDisplay) {
        						var newValue;
            					if (i == 0)
                					newValue = oneSeries[i];
                				else 
                					newValue = (oneSeries[i] * (xStepDisplay - xSteps[i - 1]) + oneSeries[i - 1] * (xSteps[i] - xStepDisplay)) / (xSteps[i] - xSteps[i - 1]);

    		    				dataTable.setValue(index, seriesIndex + 2, newValue);
            					xStepDisplay += xStepSize;
        						index++;
    						} else {
    							i++;
    						}
    					}
	    			}
    			});

    			return(dataTable);
      		};

      		function createSlider(min, max, sliderId, rangeFromId, rangeToId, callback) {
	   			$('#' + sliderId).slider({
	   				range: true,
	   				min: min,
	   				max: max,
	   				values: [ min, max ],
	   				stop: function( event, ui ) {
	   					$('#' + rangeFromId).val(ui.values[ 0 ]);
	   					$('#' + rangeToId).val(ui.values[ 1 ]);
	   					callback();
	   				}
	   			});
	   			$('#' + sliderId + ' .ui-slider-range').css('background', '#d9edf7');

	   			$('#' + rangeFromId).val(min);
	   			$('#' + rangeToId).val(max);
      		};

      		function createChart(xAxisCaption, yAxisCaption, title, containerId) {
        	    return new google.visualization.ChartWrapper({
        			'chartType': 'LineChart',
        			'containerId': containerId,
        			'options': {
//        				'width': 750,
//        			    'height': 400,
//        			    'hAxis': {'minValue': 0, 'maxValue': 60},
                  		 hAxis: {title : xAxisCaption},
                  		 vAxis: {title : yAxisCaption},
                        'title': title,
                         pointSize: 2,
                         enableInteractivity : true,
         			    'chartArea': {top: 50, left: 80, bottom: 0, right: 0, height:'70%', width:'75%'}
        			},
        		});
      		};

      		$.fn.extend({
      		    populate: function (seriesGroups) {
      		        var text = $(this).text();
    			    var newContent = "";
    			    var index = 0;
    				$.each(seriesGroups, function(i, group) {
    					newContent += "<optgroup label='" + group.groupLabel + "'>";
    					$.each(group.labels, function(j, label) {
    						newContent += "<option value='" + index + "'>" + label + "</option>";
    						index++;
    					});
    					newContent += "</optgroup>"
    				});
    				$(this).html(newContent);
    				$(this).multiselect('refresh');
      		    }
      		});
