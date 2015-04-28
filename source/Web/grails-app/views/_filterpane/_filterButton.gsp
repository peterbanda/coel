<gui:actionButton icon="icon-filter" action="javascript:void(0);" onclick="grailsFilterPane.toggleElement('${filterPaneId}');return false;" hint="Filter">
<!-- 
	<filterpane:isNotFiltered>Inactive</filterpane:isNotFiltered>
 -->
	<filterpane:isFiltered><i class="icon-ok"></i></filterpane:isFiltered>
</gui:actionButton>