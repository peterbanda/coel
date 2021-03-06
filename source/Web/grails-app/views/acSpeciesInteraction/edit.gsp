<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSpeciesInteraction.label', default: 'Species Interaction')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <theme:layout name="edit"/>
    <r:script>

		$(function(){
			$("#settingFunction\\.formula").focus()			
		});

		function showExpressionHelp() {
			$('#jep-help-modal').modal();
		}
	</r:script>
</head>
<body>
    <theme:zone name="actions">
    	<gui:actionButton action="delete" id="${instance?.id}"/>
    	<gui:actionButton action="showPrevious" id="${instance?.id}"/>
    	<gui:actionButton action="showNext" id="${instance?.id}"/>
	</theme:zone>

    <theme:zone name="details">
		<g:render template="/ac/jep_help"/>
    	<f:with bean="instance">
    		<f:ref property="action.actionSeries" textProperty="name"/>
			<f:ref property="action" textProperty="startTime"/>
		</f:with>
		<hr>
        <ui:field label="Species ← Function">
        	<ui:fieldInput>
				<g:select name="species.id" from="${species}" optionKey="id" optionValue="label" value="${instance.species.id}"/>
					&larr;
				<g:textArea class="input-xxlarge" rows="4" name="settingFunction.formula" value="${render(template:'displaySettingFunction', bean:instance)}" />
				<gui:actionButton hint="Help" onclick="showExpressionHelp(); return false;" text="?"/>
			</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>