		<g:if test="${structureImage}">
			<hr>
			<f:display bean="instance" property="id" label="Reaction Image">
				<object data="data:image/svg+xml;base64,${structureImage}"></object>
			</f:display>
		</g:if>