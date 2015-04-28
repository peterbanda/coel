<g:if test="${tds || attrs.displayEmpty}">
	<ui:table id="${tableId}">
    	<thead>
			<ui:tr>
				<g:if test="${attrs.showEnabled || attrs.editEnabled || attrs.deleteEnabled}">
					<ui:th>Do</ui:th>
				</g:if>
				<g:if test="${attrs.checkEnabled}">
                   <ui:th><g:checkBox name="checkedAll" value="" onchange="checkAll('${tableId}', this.checked);"/></ui:th>
				</g:if>
				<g:each in="${headers}" var="header">
					<ui:th>${header}</ui:th>
				</g:each>
			</ui:tr>
		</thead>
		<tbody>
		   	<g:hiddenField name="domainName" value="${attrs.domainName}"/>
			<g:if test="${ids}">
				<g:each in="${[ids,tds].transpose()}" var="idColumns">
					<g:set var="id" value="${idColumns[0]}"/>
					<g:set var="columns" value="${idColumns[1]}"/>
					<ui:tr>
						<g:hiddenField name="objectId" value="${idColumns[0]}" />

        				<g:if test="${attrs.showEnabled || attrs.editEnabled || attrs.deleteEnabled}">
							<td>
								<g:if test="${attrs.showEnabled}">
									<gui:actionLink controller="${attrs.domainName}" action="show" id="${id}"/>
								</g:if>
								<g:if test="${attrs.editEnabled}">
									<gui:actionLink controller="${attrs.domainName}" action="edit" id="${id}"/>
								</g:if>
								<g:if test="${attrs.deleteEnabled}">
									<gui:actionLink controller="${attrs.domainName}" action="delete" id="${id}"/>
								</g:if>
							</td>
						</g:if>

        				<g:if test="${attrs.checkEnabled}">
        					<td>
                            	<g:checkBox name="checked" value=""/>
							</td>
						</g:if>

						<g:each in="${columns}" var="td">
							<td>${td}</td>
						</g:each>
    				</ui:tr>
				</g:each>
			</g:if>
			<g:else>
				<g:each in="${tds}" var="columns">
					<ui:tr>
						<g:each in="${columns}" var="td">
							<td>${td}</td>
						</g:each>
    				</ui:tr>
    			</g:each>
			</g:else>
		</tbody>
	</ui:table>
</g:if>