		<g:if test="${structureImage}">
			<hr>
			<f:display bean="instance" property="id" label="Reaction Image">
				<object data="data:image/svg+xml;base64,${structureImage}" width="1100" height="200"></object>
			</f:display>
		</g:if>