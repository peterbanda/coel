<html>
<head>
    <nav:set path="app/network/Topology"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="createTemplate" icon="icon-plus" text="Template"/>
    	<gui:actionButton action="createLayered" icon="icon-plus" text="Layered"/>
    	<gui:actionButton action="createSpatial" icon="icon-plus" text="Spatial"/>
        <gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
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