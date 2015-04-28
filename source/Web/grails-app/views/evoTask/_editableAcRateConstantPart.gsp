<%@ page import="com.banda.chemistry.domain.AcRateConstantTypeBound" %>

    <r:script>
    	$(document).ready(function(){
    		$("#ac").attr("data-live-search", "true");
    		$('#ac').selectpicker({
				dropupAuto: false
			});
	    	$("#ac").bind("change", updateAcDependents);

    		$("#actionSeries").attr("data-selected-text-format", "count>2");
    		$('#actionSeries').selectpicker({
				dropupAuto: false
			});
			$('#acEvaluation').selectpicker({
				dropupAuto: false
			});

    		$("#rateConstantTypeBounds").attr("data-selected-text-format", "count>3");
    		$('#rateConstantTypeBounds').selectpicker({
				dropupAuto: false
			});

    		$("#fixedRateReactions").attr("data-selected-text-format", "count>5");
    		$('#fixedRateReactions').selectpicker({
				dropupAuto: false
			});

    		$("#fixedRateReactionGroups").attr("data-selected-text-format", "count>5");
    		$('#fixedRateReactionGroups').selectpicker({
				dropupAuto: false
			});

	    	adjustMultiSelectSizes();
	   	});

		function updateAcDependents() {
			$.getJSON('${createLink(action: "getAcRateConstantDependentData")}?id=' + $("#ac").val(),
				function(data) {
					$("#actionSeries").populateKeyValues(data.interactionSeries, false);
	      			$("#acEvaluation").populateKeyValues(data.evaluations, false);
	      			$("#fixedRateReactions").populateKeyValues(data.reactions, false);
	      			$("#fixedRateReactionGroups").populateKeyValues(data.reactionGroups, false);

	      			$('#actionSeries').selectpicker('refresh');
	      			$('#acEvaluation').selectpicker('refresh');
	      			$('#fixedRateReactions').selectpicker('refresh');
	      			$('#fixedRateReactionGroups').selectpicker('refresh');
	      			adjustMultiSelectSizes();
				});
		}

		function adjustMultiSelectSizes() {
			adjustSelectSize('actionSeries', 3, 20)
			adjustSelectSize('fixedRateReactions', 2, 5)
			adjustSelectSize('fixedRateReactionGroups', 2, 5)
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
			<ui:field bean="instance" name="rateConstantTypeBounds">
				<ui:fieldInput>			
					<g:select name="rateConstantTypeBounds" class="span6" from="${AcRateConstantTypeBound.list(sort: 'id')}"
						multiple="yes"
						optionKey="id"
						optionValue="${{it.rateConstantType.toString() + '  (' + it.bound?.from + ' - ' + it.bound?.to + ')'}}"
						size="10" 
                        value="${instance?.rateConstantTypeBounds*.id}" />
				</ui:fieldInput>
			</ui:field>

			<ui:field bean="instance" name="fixedRateReactions">
				<ui:fieldInput>			
					<g:select name="fixedRateReactions" class="span6" from="${reactions}" multiple="yes"
						optionKey="id"
						optionValue="label"
						size="5" 
                        value="${instance?.fixedRateReactions*.id}" />
				</ui:fieldInput>
			</ui:field>

			<ui:field bean="instance" name="fixedRateReactionGroups">
				<ui:fieldInput>			
					<g:select name="fixedRateReactionGroups" class="span6" from="${reactionGroups}" multiple="yes"
						optionKey="id"
						optionValue="label"
						size="5" 
                        value="${instance?.fixedRateReactionGroups*.id}" />
				</ui:fieldInput>
			</ui:field>