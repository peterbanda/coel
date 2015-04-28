	<r:script>

	    $(document).ready(function(){
	    	$("#modal-interactionSeries").attr("data-none-selected-text", "---");
	    	$('#modal-interactionSeries').selectpicker({
				dropupAuto: false
			});

			$("#modal-interaction").attr("data-none-selected-text", "---");
			$('#modal-interaction').selectpicker({
				dropupAuto: false
			});

			$("#modal-assignment").attr("data-none-selected-text", "---");
			$('#modal-assignment').selectpicker({
				dropupAuto: false
			});

			$("#modal-interactionSeries").bind("change", updateInteractions);

			$.getJSON('${createLink(action: "getAcAssignmentBounds")}?id=' + $("#id").val(),
				function(data) {
					$.each(data, function(i, bound) {
						addAssignmentBoundRowAux(bound.from, bound.to, bound.type);
						var row = $('#acAssignmentBound tr:last');
						$.each(bound.assignments, function(i, assignment) {
							addAssignmentAux(
								row,
								assignment.interactionSeriesId,
								assignment.interactionId,
								assignment.assignmentId,
								assignment.interactionSeriesLabel,
								assignment.interactionLabel,
								assignment.assignmentLabel,
								bound.type
							)
						});
					});
				});
	   	});

		function showAddAssignmentBoundDialog() {
			clearBoundModal();
			$('#modal-assignmentType').prop('disabled', false);

			setOnclick($("#assignment-bound-modal-button"), function() {
				addAssignmentBoundRow();
				lastRow = $('#acAssignmentBound tr:last');
				var type = 0;
				if ($("#modal-assignmentType").val() == 'Variable')
					type = 1;
				showAddAssignmentDialog(lastRow, type);
			})
			$("#assignment-bound-modal" ).modal({ keyboard: true });
		}

		function showEditAssignmentBoundDialog(row) {
			clearBoundModal();

			var from = row.find("td").eq(2).html();
			var to = row.find("td").eq(3).html();

			$('#modal-from').val(from);
			$('#modal-to').val(to);
			$('#modal-assignmentType').prop('disabled', true);

			setOnclick($("#assignment-bound-modal-button"), function() {updateAssignmentBoundRow(row)})
			$("#assignment-bound-modal" ).modal({ keyboard: true });
		}

		function removeAssignmentBound(row) {
			row.remove();
		}

		function addAssignmentBoundRow() {
			var typeVal = $("#modal-assignmentType").val();
			var from = $("#modal-from").val();
			var to = $("#modal-to").val();

			var type = 0;
			if (typeVal == "Variable")
				type = 1;

			addAssignmentBoundRowAux(from, to, type);
		}

		function addAssignmentBoundRowAux(from, to, type) {
			var rowCount = $('#acAssignmentBound tr').length + 1;
			var rowId = rowCount - 2
			var clazz
			if (rowId % 2 == 0)
				clazz = 'odd'
			else
				clazz = 'even'

			var newRow = $('<tr class="' + clazz + '">');

			var doColumn = $('<td>');
			newRow.append(doColumn);

			var editLink = $('<a href="javascript:void(0);"><i class="icon-edit"></i></a>');
			var removeLink = $('<a href="javascript:void(0);"><i class="icon-trash"></i></a>');
			var addAssignmentLink = $('<a href="javascript:void(0);"><i class="icon-plus"></i></a>');

			editLink.click(function() {showEditAssignmentBoundDialog(newRow)});
			removeLink.click(function() {removeAssignmentBound(newRow)});
			addAssignmentLink.click(function() {showAddAssignmentDialog(newRow, type)});

			doColumn.append(editLink);
			doColumn.append(removeLink);
			doColumn.append(addAssignmentLink);

			if (type == 0)
				newRow.append('<td>Species</td>');
			else
				newRow.append('<td>Variable</td>');

			newRow.append('<td>' + from + '</td>');
			newRow.append('<td>' + to + '</td>');

			var assignmentColumn = $('<td>');
			var assignmentUl = $('<ul id="assignments">')
			assignmentColumn.append(assignmentUl)
			newRow.append(assignmentColumn)
	
			$("#acAssignmentBound").append(newRow);				
		}

		function updateAssignmentBoundRow(row) {
			var from = $("#modal-from").val()
			var to = $("#modal-to").val()

			row.find("td").eq(2).html(from);
			row.find("td").eq(3).html(to);
		}

		function showAddAssignmentDialog(row, type) {
			clearAssignmentModal();

			setOnclick($("#assignment-modal-button"), function() {addAssignment(row, type)})
			if (type == 0)
				setOnchange($("#modal-interaction"), updateSpeciesAssignments)
			else
				setOnchange($("#modal-interaction"), updateVariableAssignments)

			$("#assignment-modal" ).modal({ keyboard: true });
		}

		function showEditAssignmentDialog(assignment, type) {
			$('#modal-interactionSeries').val(assignment.find('#interactionSeriesId').val());
			$('#modal-interactionSeries').selectpicker('refresh');

			updateInteractions();
			$('#modal-interaction').val(assignment.find('#interactionId').val());
			$('#modal-interaction').selectpicker('refresh');

			if (type == 0)
				updateSpeciesAssignments();
			else
				updateVariableAssignments();

	      	$('#modal-assignment').val(assignment.find('#assignmentId').val());
			$('#modal-assignment').selectpicker('refresh');

			setOnclick($("#assignment-modal-button"), function() {updateAssignment(assignment, type)})
			$("#assignment-modal" ).modal({ keyboard: true });
		}

		function addAssignment(row, type) {
			var interactionSeriesId = $("#modal-interactionSeries").val()
			var interactionId = $("#modal-interaction").val()
			var assignmentId = $("#modal-assignment").val()

			var interactionSeriesLabel = $("#modal-interactionSeries :selected").text();
			var interactionLabel = $("#modal-interaction :selected").text();
			var assignmentLabel = $("#modal-assignment :selected").text();

			addAssignmentAux(row, interactionSeriesId, interactionId, assignmentId, interactionSeriesLabel, interactionLabel, assignmentLabel, type);
		}

		function addAssignmentAux(
			row,
			interactionSeriesId, interactionId, assignmentId, interactionSeriesLabel, interactionLabel, assignmentLabel, type
		) {
			var assignmentUl = row.find('#assignments');
			var assignment = $('<li id="assignment">');
			assignmentUl.append(assignment);

			assignment.append('<input type="hidden" id="interactionSeriesId" name="interactionSeriesId" value="' + interactionSeriesId + '"/>');
			assignment.append('<input type="hidden" id="interactionId" name="interactionSeriesId" value="' + interactionId + '" />');
			assignment.append('<input type="hidden" id="assignmentId" name="assignmentId" value="' + assignmentId + '" />');

			var editLink = $('<a href="javascript:void(0);">' + assignmentLabel + ' > ' + interactionLabel + ' > ' + interactionSeriesLabel + '</a>');
			editLink.click(function() {showEditAssignmentDialog(assignment, type)});

			assignment.append(editLink);				
		}

		function updateAssignment(assignment, type) {
			var interactionSeriesId = $("#modal-interactionSeries").val()
			var interactionId = $("#modal-interaction").val()
			var assignmentId = $("#modal-assignment").val()

			var interactionSeriesLabel = $("#modal-interactionSeries :selected").text();
			var interactionLabel = $("#modal-interaction :selected").text();
			var assignmentLabel = $("#modal-assignment :selected").text();

			assignment.html("")
			assignment.append('<input type="hidden" id="interactionSeriesId" name="interactionSeriesId" value="' + interactionSeriesId + '"/>');
			assignment.append('<input type="hidden" id="interactionId" name="interactionSeriesId" value="' + interactionId + '" />');
			assignment.append('<input type="hidden" id="assignmentId" name="assignmentId" value="' + assignmentId + '" />');

			var editLink = $('<a href="javascript:void(0);">' + assignmentLabel + ' > ' + interactionLabel + ' > ' + interactionSeriesLabel + '</a>');
			editLink.click(function() {showEditAssignmentDialog(assignment, type)});

			assignment.append(editLink);
		}

		function clearBoundModal() {
			$('#modal-from').val("");
			$('#modal-to').val("");
		}

		function clearAssignmentModal() {
			$("#modal-interactionSeries").selectpicker('deselectAll');
			$("#modal-interaction").selectpicker('deselectAll');
			$("#modal-assignment").selectpicker('deselectAll');

			$("#modal-interaction").populateKeyValues([], true);
	      	$('#modal-interaction').prop('disabled', true);

	      	$("#modal-assignment").populateKeyValues([], true);
	      	$('#modal-assignment').prop('disabled', true);

			$('#modal-interactionSeries').selectpicker('refresh');
	      	$('#modal-interaction').selectpicker('refresh');
	      	$('#modal-assignment').selectpicker('refresh');
		}

		function updateInteractions() {
			if ($("#modal-interactionSeries").val()) {
				// cannot use getJSON since this call must be synchronous
				$.ajax({
  					url: '${createLink(action: "getAcInteractions")}?id=' + $("#modal-interactionSeries").val(),
  					dataType: 'json',
 					async: false,
  					success: function(data) {
  						$('#modal-interaction').prop('disabled', false);
						$("#modal-interaction").populateKeyValues(data, true);
	      				$('#modal-interaction').selectpicker('refresh');

						disableSelect($("#modal-assignment"));
					}
				});
			} else {
				disableSelect($("#modal-interaction"));
				disableSelect($("#modal-assignment"));
			}
		}

		function updateSpeciesAssignments() {
			if ($("#modal-interaction").val()) {
				// cannot use getJSON since this call must be synchronous
				$.ajax({
  					url: '${createLink(action: "getAcSpeciesAssignments")}?id=' + $("#modal-interaction").val(),
  					dataType: 'json',
 					async: false,
  					success: function(data) {
						$('#modal-assignment').prop('disabled', false);
						$("#modal-assignment").populateKeyValues(data, true);
	      				$('#modal-assignment').selectpicker('refresh');
					}
				});
			} else
				disableSelect($("#modal-assignment"));
		}

		function updateVariableAssignments() {
			if ($("#modal-interaction").val()) {
				// cannot use getJSON since this call must be synchronous
				$.ajax({
  					url: '${createLink(action: "getAcVariableAssignments")}?id=' + $("#modal-interaction").val(),
  					dataType: 'json',
 					async: false,
  					success: function(data) {
						$('#modal-assignment').prop('disabled', false);
						$("#modal-assignment").populateKeyValues(data, true);
	      				$('#modal-assignment').selectpicker('refresh');
					}
				});
			} else
				disableSelect($("#modal-assignment"));
		}

		function disableSelect(element) {
			element.populateKeyValues([], true);
	      	element.prop('disabled', true);
	      	element.selectpicker('refresh');
	    }

		function getTableAssignmentBounds() {
		    return $('#acAssignmentBound tr:gt(0)').map(function () {
       			var this_row = $(this);
       			var typeValue = $.trim(this_row.find('td:eq(1)').html());
       			var type = 0;
       			if (typeValue == 'Variable')
       				type = 1;
        		var fromValue = $.trim(this_row.find('td:eq(2)').html());
        		var toValue = $.trim(this_row.find('td:eq(3)').html())
        		var assignmentIds = this_row.find('#assignments li').map(function () {
        			var this_assignment = $(this);
        			return this_assignment.find('#assignmentId').val()        				 
        		}).get();

        		return {
        			type : type,
        			from : fromValue,
        			to : toValue,
       				assignments : assignmentIds
        		};
    		}).get();
    	}

		function setOnclick(element, fun) {
			element.removeAttr('onclick');
			element.unbind('click');
			element.click(fun);
		}

		function setOnchange(element, fun) {
			element.unbind('change.custom');
			element.bind('change.custom', fun);
		}

	</r:script>

    	<gui:modal id="assignment-bound-modal" title="Bound" onclick="addAssignmentBoundRow()">
			<div class="row-fluid">
				<div class="span10">

					<ui:field label="Type">
						<ui:fieldInput>
							<g:select name="modal-assignmentType" from="${["Species", "Variable"]}"/>
						</ui:fieldInput>
					</ui:field>

				 	<ui:field label="From">
						<ui:fieldInput>
							<g:textField name="modal-from" value=""/>
        	    	    </ui:fieldInput>
                   	</ui:field>

				 	<ui:field label="To">
						<ui:fieldInput>
							<g:textField name="modal-to" value=""/>
        	    	    </ui:fieldInput>
                    </ui:field>
 				</div>
 			</div>
		</gui:modal>

    	<gui:modal id="assignment-modal" title="Assignment" onclick="addAssignment()">
			<div class="row-fluid">
				<div class="span10">
					<ui:field label="Interaction Series">
						<ui:fieldInput>
							<g:select name="modal-interactionSeries" class="span10"
								from="${allInteractionSeries}"
								optionKey="id"
								optionValue="${{it.id + ' : ' + it.name}}"/>
						</ui:fieldInput>
					</ui:field>

				 	<ui:field label="Interaction">
						<ui:fieldInput>
							<g:select name="modal-interaction" class="span5" from=""/>
        	    	    </ui:fieldInput>
                    </ui:field>

				 	<ui:field label="Assignment">
						<ui:fieldInput>
							<g:select name="modal-assignment" class="span10" from=""/>
        	    	    </ui:fieldInput>
                    </ui:field>
 				</div>
 			</div>
		</gui:modal>

				<ui:field bean="instance" name="assignmentBounds">
					<ui:fieldInput>
						<div class="row-fluid span10">
                			<div class="pull-right">
								<a href="javascript:void(0);" class="create" onclick="showAddAssignmentBoundDialog();"/>
									<i class="icon-plus"></i>
									<g:message code="evoAcInteractionSeriesTask.addAssignmentBound.label" default="Bound" />
								</a>
							</div>
						</div>
						<div class="row-fluid span10">
							<gui:table id="acAssignmentBound" domainName="acAssignmentBound" displayEmpty="true">
								<gui:column label="Do"/>
								<gui:column label="Type"/>
								<gui:column label="From"/>
								<gui:column label="To"/>
       							<gui:column label="Assignments"/>
							</gui:table>
						</div>
					</ui:fieldInput>
				</ui:field>