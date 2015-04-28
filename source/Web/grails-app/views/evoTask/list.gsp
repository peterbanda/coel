<html>
<head>
    <nav:set path="app/evolution/EvolutionTask"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="actions">
    	<span class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
  				<i class="icon-plus"></i>
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">  			
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcRateConstantTaskInstance')}">AC Rate Constant Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcInteractionSeriesTaskInstance')}">AC Interaction Series Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createAcSpecInstance')}">AC Spec Type</a></li>
    			<li role="presentation"><a role="menuitem" href="${createLink(action: 'createNetworkInstance')}">Network Type</a></li>
  			</ul>
		</span>
        <gui:actionButton action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('evoTaskTable','deleteMultiple');return false;"/>
        <gui:actionButton action="copy" hint="Copy" onclick="doTableSelectionAction('evoTaskTable','copyMultiple');return false;"/>
		<filterpane:filterButton/>
		<filterpane:filterPane domain="${className}"/>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
        	<gui:column property="name" editable="true"/>
            <gui:column property="createTime"/>
            <gui:column property="taskType"/>
        </gui:table>
    </theme:zone>
</body>
</html>
