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
		});

		function showShowMultipleDialog() {
			var ids = getCheckedIds("evoRunTable")
			$('#multipleShowForm').find('#ids').val(ids.toString())
			$( "#dialog-show-multiple-form" ).dialog( "open" );
		}
	</r:script>

        <div id="dialog-show-multiple-form" title="Show Multiple Evolutions">		
			<ui:form name="multipleShowForm" action="showMulti" method="POST">
			    <g:hiddenField name="ids" value="" />

				<ui:field label="Stats Type">
					<ui:fieldInput>
						<g:select name="statsType" from="${StatsType?.values()}" noSelection= "['': '---']"/>
					</ui:fieldInput>
				</ui:field>
			</ui:form>
		</div>