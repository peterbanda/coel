<html>
<head>
    <nav:set path="app/chemistry/Compartment"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="create" hint="Add New"/>
        <gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
       	<gui:actionButton action="copy" hint="Copy" onclick="doTableSelectionAction('acCompartmentTable','copyMultiple');return false;"/>
       	<filterpane:filterButton/>
		<filterpane:filterPane domain="${className}" excludeProperties="createTime"/>
	</theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
       		<gui:column property="label" editable="true"/>
            <gui:column property="reactionSet"><g:link controller="acReactionSet" action="show" id="${it?.id}">${it.label}</g:link></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>