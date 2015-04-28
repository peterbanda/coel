<%@ page import="com.banda.math.domain.StatsType" %>

	<r:script>
		$(function(){

			$( "#dialog-show-multiple-form" ).dialog({
				autoOpen: false,
				resizable: true,
				height:200,
				width:450,
				modal: true,
				buttons: {
					Submit: function() {
						$("#multipleShowForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});

			$( "#dialog-export-as-csv-form" ).dialog({
				autoOpen: false,
				resizable: true,
				height:200,
				width:450,
				modal: true,
				buttons: {
					Submit: function() {
						$("#exportAsCsvForm").submit();
						$( this ).dialog( "close" );
					},
					Cancel: function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});

		function showShowMultipleDialog() {
			var ids = getCheckedIds("networkDamageSpreadingTable")
			$('#multipleShowForm').find('#ids').val(ids.toString())
			$( "#dialog-show-multiple-form" ).dialog( "open" );
		}

		function showExportAsCsvDialog() {
			var ids = getCheckedIds("networkDamageSpreadingTable")
			$('#exportAsCsvForm').find('#ids').val(ids.toString())
			$( "#dialog-export-as-csv-form" ).dialog( "open" );
		}
	</r:script>

        <div id="dialog-show-multiple-form" title="Show Net Damage Spreading">		
			<ui:form name="multipleShowForm" action="showMulti" method="POST">
			    <g:hiddenField name="ids" value="" />

				<ui:field label="Stats Type">
					<ui:fieldInput>
						<g:select name="statsType" from="${StatsType?.values()}" noSelection= "['': '---']"/>
					</ui:fieldInput>
				</ui:field>
			</ui:form>
		</div>

		<div id="dialog-export-as-csv-form" title="Export Net Damage Spreading">		
			<ui:form name="exportAsCsvForm" action="exportAsCSV" method="POST">
			    <g:hiddenField name="ids" value="" />

				<ui:field label="Stats Type">
					<ui:fieldInput>
						<g:select name="statsType" from="${StatsType?.values()}" noSelection= "['': '---']"/>
					</ui:fieldInput>
				</ui:field>
			</ui:form>
		</div>