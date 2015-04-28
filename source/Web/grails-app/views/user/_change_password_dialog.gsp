    <r:script>
			function showChangePasswordDialog() {
				$("#change-password-modal").modal();
			}
	</r:script>

		<gui:modal id="change-password-modal" title="Change Password" controller="user" action="changePassword">
			<g:hiddenField name="id" value="${instance?.id}" />

			<ui:field label="New Password">
				<ui:fieldInput>
				    <g:field type="password" name="password" value=""/>
				</ui:fieldInput>
			</ui:field>
		</gui:modal>