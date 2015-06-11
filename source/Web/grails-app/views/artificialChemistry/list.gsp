<html>
<head>
    <nav:set path="app/chemistry/ArtificialChemistry"/>
    <r:require module="jquery-ui"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="actions">
        <gui:actionButton action="create" hint="Add New"/>
        <gui:actionButton action="createQuick" icon="icon-plus" text="Quick" hint="Add New Quick"/>
		<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
       	<gui:actionButton action="copy" hint="Copy" onclick="doTableSelectionAction('artificialChemistryTable','copyMultiple');return false;"/>
       	<filterpane:filterButton/>
		<filterpane:filterPane domain="${className}" excludeProperties="createTime"/>
	</theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showGenerateACsDialog();">Generate ACs</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showSBMLDialog();">Import as SBML</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
        	<gui:column property="name" editable="true"/>
            <gui:column property="createTime"/>
        </gui:table>
    </theme:zone>
</body>
</html>