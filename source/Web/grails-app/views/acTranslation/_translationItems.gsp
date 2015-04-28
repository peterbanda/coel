<ul class="inline">
    <li>Items</li>
    <li>
        <div class="spacedTop spacedLeft">
            <gui:actionLink controller="acTranslationItem" action="create" params="['translation.id':instance.id]" hint="Add New"/>
            <gui:actionLink action="delete" onclick="if (confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')) doTableSelectionAction('acTranslationItemTable','deleteMultiple');return false;" hint="Delete"/>
        </div>
        <gui:table list="${instance?.translationItems.sort { it.variable.label }}" showEnabled="true" editEnabled="true" checkEnabled="true">
            <gui:column label="Variable &larr; Function">
                <g:render template="displayTranslationItemFunction" bean="${bean}" />
            </gui:column>
            <gui:column label="Ref Species">
                <g:render template="displayTranslationItemRefSpecies" bean="${bean}" />
            </gui:column>
            <g:if test="${instance.translationSeries.variablesReferenced}">
                <gui:column label="Ref Variables">
                    <g:render template="displayTranslationItemRefVariables" bean="${bean}" />
                </gui:column>
            </g:if>
         </gui:table>
    </li>
</ul>
