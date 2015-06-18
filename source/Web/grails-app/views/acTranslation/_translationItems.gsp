<ul class="inline">
    <li>Items</li>
    <li>
        <div class="spacedTop spacedLeft">
            <g:if test="${!instance?.translationItems.isEmpty()}">
                <gui:actionLink controller="acTranslationItem" action="create" params="['translation.id':instance.id]" hint="Add New"/>
                <gui:modal id="confirm-translationItem-delete-modal" title="Delete" onclick="doTableSelectionAction('acTranslationItemTable','deleteMultiple')" text="Are you sure?"/>
                <gui:actionLink icon="icon-trash" onclick="openModal('confirm-translationItem-delete-modal')" hint="Delete"/>
            </g:if>
            <g:else>
                <gui:actionLink controller="acTranslationItem" action="create" params="['translation.id':instance.id]" hint="Start here to add a new item" hint-placement="right" hint-show="true"/>
            </g:else>
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
