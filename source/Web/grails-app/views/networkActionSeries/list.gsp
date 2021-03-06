<html>
<head>
    <nav:set path="app/network/InteractionSeries"/>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:require module="jquery-ui"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="extras">
	    <g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showCopyMultipleDialog();">Copy Multiple Interaction Series</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
       		<gui:column property="name" editable="true"/>
            <gui:column property="timeCreated"/>
        </gui:table>
    </theme:zone>
</body>
</html>