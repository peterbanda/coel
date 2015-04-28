<%@ page import="com.banda.math.domain.StatsType" %>
<%@ page import="com.banda.math.domain.dynamics.SingleRunAnalysisResultType" %>

        <r:script>

			// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore! 
			$( "#dialog:ui-dialog" ).dialog( "destroy" );

        	$(document).ready(function() {

				$( "#dialog-display-fitness-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:230,
					modal: true,
					buttons: {
						Display: function() {
							$("#displayFitnessButton").click();
							$( this ).dialog( "close" );
						},
						Export: function() {
							$("#export").val("true");
							$("#exportFitnessButton").click();
							$( this ).dialog( "close" );
						},
						
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});

				$( "#dialog-display-fitness-correlation-form" ).dialog({
					autoOpen: false,
					resizable: false,
					width:400,
					height:230,
					modal: true,
					buttons: {
						Display: function() {
							$("#displayFitnessCorrelationButton").click();
							$( this ).dialog( "close" );
						},
						Export: function() {
							$("#export2").val("true");
							$("#exportFitnessCorrelationButton").click();
							$( this ).dialog( "close" );
						},
						
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
        	});

			function showDisplayFitnessDialog() {
				$( "#dialog-display-fitness-form" ).dialog( "open" );
			}

			function showDisplayFitnessCorrelationDialog() {
				$( "#dialog-display-fitness-correlation-form" ).dialog( "open" );
			}

		</r:script>

		<div id="dialog-display-fitness-form" title="Display Fitness">		
			<g:form name="displayFitnessForm" >
			    <g:hiddenField name="id" value="${instance?.id}" />
			    <g:hiddenField name="export" value="false" />
                <tr class="prop">
                    <td valign="top" class="name">
                    	<label for="statsType"><g:message code="evoTask.statsType.label" default="Stats Type" /></label>
                    </td>
                    <td valign="top" class="value">
                    	<g:select name="statsType" from="${StatsType?.values()}" value="${StatsType?.Mean}"/>
                	</td>
                </tr>
                <div class="buttons" hidden="true">
                	<g:submitToRemote id="displayFitnessButton" url="[controller: 'evoTask', action:'displayFitness']" update="PlotImageDiv"/>
                    <g:actionSubmit id="exportFitnessButton" action="displayFitness" value="x"/>
                </div>
			</g:form>
		</div>
		<div id="dialog-display-fitness-correlation-form" title="Display Fitness Correlation">		
			<g:form name="displayFitnessCorrelationForm" >
			    <g:hiddenField name="id" value="${instance?.id}" />
			    <g:hiddenField name="export2" value="false" />
                <tr class="prop">
                    <td valign="top" class="name">
                    	<label for="chromElementIndex"><g:message code="evoTask.chromElementIndex.label" default="Chrom Element Index" /></label>
                    </td>
                    <td valign="top" class="value">
                    	<g:textField name="chromElementIndex" value="0" />
                	</td>
                </tr>
                <div class="buttons" hidden="true">
                	<g:submitToRemote id="displayFitnessCorrelationButton" url="[controller: 'evoTask', action:'displayFitnessCorrelation']" update="PlotImageDiv"/>
                    <g:actionSubmit id="exportFitnessCorrelationButton" action="displayFitnessCorrelation" value="x"/>
                </div>
			</g:form>
		</div>