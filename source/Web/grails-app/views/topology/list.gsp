<html>
<head>
    <nav:set path="app/network/Topology"/>
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
                <li role="presentation"><a role="menuitem" href="${createLink(action: 'createSpatial')}">Spatial</a></li>
                <li role="presentation"><a role="menuitem" href="${createLink(action: 'createTemplate')}">Template</a></li>
                <li role="presentation"><a role="menuitem" href="${createLink(action: 'createLayered')}">Layered</a></li>
            </ul>
        </span>
        <gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
        <gui:actionButton action="copy" hint="Copy" onclick="doTableSelectionAction('topologyTable','copyMultiple');return false;"/>
		<filterpane:filterButton/>
		<filterpane:filterPane domain="${className}"/>
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