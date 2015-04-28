			<f:display bean="instance" property="id" label="Type">
				<g:if test="${instance?.isTemplate()}">
					</b><g:message code="topology.templateType.label" default="Template" />
					<g:hiddenField name="type" value="template" />
				</g:if>
				<g:else>
					<g:if test="${instance?.isSpatial()}">
						</b><g:message code="topology.spatialType.label" default="Spatial" />
						<g:hiddenField name="type" value="spatial" />
					</g:if>
					<g:else>
						<g:if test="${instance?.supportLayers()}">
							<g:message code="topology.layeredType.label" default="Layered" />
							<g:hiddenField name="type" value="layered" />
						</g:if>
						<g:else>
							<g:message code="topology.flatType.label" default="Flat" />
							<g:hiddenField name="type" value="flat" />
						</g:else>
					</g:else>
				</g:else>
			</f:display>