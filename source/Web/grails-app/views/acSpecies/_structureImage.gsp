		<g:if test="${structureImage}">
			<hr>
			<f:display bean="instance" property="structure" label="Structure Image">
				<object data="data:image/svg+xml;base64,${structureImage}" width="400" height="200"></object>
			</f:display>
		</g:if>