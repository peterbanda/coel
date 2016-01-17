<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteractionVariableAssignment.label', default: 'Variable Assignment')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <theme:layout name="create"/>
    <r:script>

		$(function(){
			$("#variable").focus()			
		});

		function showExpressionHelp() {
			$('#jep-help-modal').modal();
		}
	</r:script>
</head>
<body>
    <theme:zone name="details">
		<g:render template="/ac/jep_help"/>
        <g:hiddenField name="action.id" value="${instance.action.id}" />
    	<f:with bean="instance">
    		<f:ref property="action.actionSeries" textProperty="name"/>
			<f:ref property="action" textProperty="startTime"/>
		</f:with>
		<hr>
		<ui:field label="Variable ← Function">
        	<ui:fieldInput>
				<g:select name="variable.id" from="${interactionVariables}" optionKey="id" optionValue="label" value="${instance.variable?.id}"/>
					&larr;
				<g:textArea class="input-xxlarge" rows="4" name="settingFunction.formula" value="${render(template:'displaySettingFunction', bean:instance)}" />
				<gui:actionButton hint="Help" onclick="showExpressionHelp(); return false;" text="?"/>
			</ui:fieldInput>
        </ui:field>
    </theme:zone>

    <theme:zone name="cancelButton">
        <ui:button kind="anchor" controller="acInteraction" action="show" id="${instance.action.id}">${message(code: 'default.button.cancel.label', default: 'Cancel')}</ui:button>
    </theme:zone>
</body>
</html>