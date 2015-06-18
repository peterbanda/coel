<g:if test="${!icon}">
    <g:if test="${action == 'list'}">
        <g:set var="icon" value="icon-th-list" />
    </g:if>
    <g:if test="${action == 'create'}">
        <g:set var="icon" value="icon-plus" />
    </g:if>
    <g:if test="${action == 'edit'}">
        <g:set var="icon" value="icon-edit" />
    </g:if>
    <g:if test="${action == 'show'}">
        <g:set var="icon" value="icon-eye-open" />
    </g:if>
    <g:if test="${action == 'delete'}">
        <g:set var="icon" value="icon-trash" />
    </g:if>
    <g:if test="${action == 'copy'}">
        <g:set var="icon" value="icon-plus-sign" />
    </g:if>
    <g:if test="${action == 'showPrevious'}">
        <g:set var="icon" value="icon-arrow-left" />
    </g:if>
    <g:if test="${action == 'showNext'}">
        <g:set var="icon" value="icon-arrow-right" />
    </g:if>
</g:if>

<g:if test="${hint}">
    <g:if test="${!hintplacement}">
        <g:set var="hintplacement" value="bottom" />
    </g:if>

	<p:callTag tag="ui:button" kind="anchor" attrs="${attrs}" data-content="${hint}" rel="popover" data-placement="${hintplacement}" data-trigger="hover">
        <g:if test="${icon}">
            <i class="${icon}"></i>
        </g:if>
        ${title}
    </p:callTag>
</g:if>
<g:else>
    <p:callTag tag="ui:button" kind="anchor" attrs="${attrs}">
        <g:if test="${icon}">
            <i class="${icon}"></i>
        </g:if>
        ${title}
    </p:callTag>
</g:else>