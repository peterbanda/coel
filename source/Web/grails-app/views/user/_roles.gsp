<%@ page import="com.banda.core.domain.um.Role" %>

		<sec:ifAnyGranted roles="ROLE_ADMIN">
			<ui:field bean="instance" name="roles">
				<ui:fieldInput>
				<g:select name="roles" from="${Role.list()}"
					multiple="yes"
					optionKey="id"
					optionValue="name"
					size="5"
					value="${instance?.roles*.id}" />
				</ui:fieldInput>
			</ui:field>
		</sec:ifAnyGranted>