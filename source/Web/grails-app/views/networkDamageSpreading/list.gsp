<html>
<head>
	<nav:set path="app/network/DamageSpreading"/>
	<r:require module="jquery-ui"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="filter">
 		<filterpane:filterPane domain="${className}" associatedProperties="network.name"/>
    </theme:zone>

	<theme:zone name="extras">
		<g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showExportAsCsvDialog();">Export as CSV</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showShowMultipleDialog();">Show</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
            <gui:column property="timeCreated"/>
            <gui:column property="network"><g:link controller="post" action="show" id="${it.id}">${it.name}</g:link></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>