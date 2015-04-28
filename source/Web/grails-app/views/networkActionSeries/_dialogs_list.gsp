        <r:script>

			$(function(){

				$( "#dialog-copy-multiple-form" ).dialog({
					autoOpen: false,
					resizable: true,
					height:350,
					modal: true,
					buttons: {
						Submit: function() {
							$("#multipleActionSeriesCopyForm").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});
			});

			function showCopyMultipleDialog() {
				$( "#dialog-copy-multiple-form" ).dialog( "open" );
			}
		</r:script>

        <div id="dialog-copy-multiple-form" title="Copy Multiple Interaction Series">		
			<g:form name="multipleActionSeriesCopyForm" action="copyMultiple" method="POST">
                <g:select name="actionSeriesIds" from="${list}"
                	multiple="yes"
                	optionKey="id"
                	optionValue="${{it.id + " : " + it.name}}"
                	size="20"/>
			</g:form>
		</div>

