<html>
<head>
    <nav:set path="app/chemistry/InteractionSeries"/>
    <r:require module="editable"/>
	<theme:layout name="list"/>
<head>
<body>
	<theme:zone name="actions">
		<g:if test="${list.isEmpty()}">
			<gui:actionButton action="create" hint="Add New (Start Here)" hint-show="true" />
		</g:if>
		<g:else>
			<gui:actionButton action="create" hint="Add New"/>
		</g:else>
		<gui:actionButton action="delete" hint="Delete" onclick="openModal('confirm-delete-modal'); return false;"/>
       	<gui:actionButton action="copy" hint="Copy" onclick="showCopyMultipleDialog();return false;"/>
       	<filterpane:filterButton/>
		<filterpane:filterPane domain="${className}" excludeProperties="varSequenceNum, repeatFromElement, timeCreated"/>
	</theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_list"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showAddInteractionsMultipleDialog();">Add Interactions</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="table">
        <gui:table list="${list}" showEnabled="true" editEnabled="true" checkEnabled="true">
        	<gui:column property="id"/>
        	<gui:column property="name" editable="true"/>
            <gui:column property="timeCreated"/>
            <gui:column property="speciesSet"><g:link controller="acSpeciesSet" action="show" id="${it.id}">${it.name}</g:link></gui:column>
        </gui:table>
    </theme:zone>
</body>
</html>