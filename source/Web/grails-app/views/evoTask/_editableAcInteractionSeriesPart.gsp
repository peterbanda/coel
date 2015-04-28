    <r:script>
    	$(document).ready(function(){
    		$("#ac").attr("data-live-search", "true");
    		$('#ac').selectpicker({
				dropupAuto: false
			});
//    		$('select').selectpicker({
//				dropupAuto: false
//			});
	    	$("#ac").bind("change", updateAcDependents);

    		$("#actionSeries").attr("data-selected-text-format", "count>2");
    		$('#actionSeries').selectpicker({
				dropupAuto: false
			});
			$('#acEvaluation').selectpicker({
				dropupAuto: false
			});

			// modal interaction series combo
			$("#interactionSeries").attr("data-selected-text-format", "count>2");
    		$('#interactionSeries').selectpicker({
				dropupAuto: false
			});

	    	adjustMultiSelectSizes();

			$('form').on('submit', function(e){
				var assignmentBoundTableJSON = JSON.stringify(getTableAssignmentBounds());
				$("#assignmentBoundTable").val(assignmentBoundTableJSON);
			});
	   	});

		function updateAcDependents() {
			$.getJSON('${createLink(action: "getAcInteractionSeriesDependentData")}?id=' + $("#ac").val(),
				function(data) {
					$("#actionSeries").populateKeyValues(data.topInteractionSeries, false);
	      			$("#acEvaluation").populateKeyValues(data.evaluations, false);
	      			$("#modal-interactionSeries").populateKeyValues(data.interactionSeries, true);

	      			$('#actionSeries').selectpicker('refresh');
	      			$('#acEvaluation').selectpicker('refresh');
	      			$('#modal-interactionSeries').selectpicker('refresh');
	      			adjustMultiSelectSizes();
				});
		}

		function adjustMultiSelectSizes() {
			adjustSelectSize('actionSeries', 3, 20)
		}

		function adjustSelectSize(selectId, min, max) {
			var newSize = $('select#' + selectId + ' option').length
			if (newSize > max) newSize = max
			if (newSize < min) newSize = min
			$('#' + selectId).attr('size', newSize);
		}

    </r:script>

			<ui:field bean="instance" name="ac" from="${acs}" class="span6" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
			<ui:field bean="instance" name="actionSeries" label="Interaction Series">
				<ui:fieldInput>
					<g:select name="actionSeries" class="span6" multiple="yes"
						from="${interactionSeries}"
						optionKey="id"
						optionValue="${{it.id + ' : ' + it.name}}"
						value="${instance?.actionSeries*.id}"
					/>
				</ui:fieldInput>
			</ui:field>
			<ui:field bean="instance" name="acEvaluation" class="span6" from="${evaluations}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
			<ui:field bean="instance" name="asRepetitions" label="Inter. Series Repetitions"/>
			<ui:field bean="instance" name="runSteps" label="Run Time"/>
			<ui:field bean="instance" name="lastEvaluationStepsToCount" label="Last Eval. Iterations To Count"/>

			<g:hiddenField name="assignmentBoundTable" value="" />
			<g:render template="assignmentBounds"/>
