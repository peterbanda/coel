<html>
<head>
    <nav:set path="app/chemistry/ReactionSet"/>
    <r:require module="jquery-ui"/>
    <r:require module="ddslick"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
	<r:script>
				$(function(){

				$( "#dialog:ui-dialog" ).dialog( "destroy" );

				$( "#jasper-export-dialog" ).dialog({
					autoOpen: false,
					resizable: true,
					height:320,
					width:170,
					modal: true,
					buttons: {
						Submit: function() {
							$('#_format').ddslick('destroy');
							$("#reactionSet").submit();
							$( this ).dialog( "close" );
						},
						Cancel: function() {
							$( this ).dialog( "close" );
						}
					}
				});	
			});

			function showJasperDialog() {
		    	$('#_format').ddslick({
		    		width: 90,
		    		imagePosition: "left",
		    		selectText: "Select export format",
		    	});
				$( "#jasper-export-dialog" ).dialog( "open" );
			}
	</r:script>
<head>
<body>
	<theme:zone name="extras">
	<!-- 
		<g:jasperDialogReportScript id="reactionSet"/>
 -->
       	<g:jasperDialogReport
        	id="reactionSet"
        	controller="acReactionSet"
       		action="createReport"
       		jasper="reactionSet"
      		format="PDF,HTML,XML,CSV,XLS,RTF,TEXT,ODT,ODS,DOCX,XLSX,PPTX"
            name="ac_reaction_sets">
       	</g:jasperDialogReport>
       	<g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showJasperDialog();">Export</a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showAddReactionsDialog();">Add Reactions</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
        	<gui:column property="label" editable="true"/>
            <gui:column property="createTime"/>
        </gui:table>
    </theme:zone>
</body>
</html>