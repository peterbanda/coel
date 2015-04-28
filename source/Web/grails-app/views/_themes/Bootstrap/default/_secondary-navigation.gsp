<!-- 
<ui:secondaryNavigation/>
 -->

<nav:secondary class="nav nav-pills nav-stacked spacedTopMedium" custom="true">
    <g:if test="${item.data.divider}">
    	<li class="divider"></li>
    </g:if>
    <g:elseif test="${item.data.header}">
    	<li class="nav-header nav-header-left"><nav:title item="${item}"/></li>
    </g:elseif>
	<g:else>
    	<li class="${active ? 'active' : ''}">
    		<p:callTag tag="g:link" attrs="${linkArgs}">
    			<g:if test="${item.data.icon}">
    					<i class="${item.data.icon}"></i>
    				<span>
    					<nav:title item="${item}"/>
    				</span>
    			</g:if>
    			<g:else>
					<nav:title item="${item}"/>
				</g:else>
        	</p:callTag>
    	</li>
    </g:else>
</nav:secondary>


