    <r:script>

		$(function(){
			$("#advancedDiv").hide()
			$("#hideAdvancedButton").hide()
			$("#name").focus()			
		});

  		function showAdvanced() {
			$("#advancedDiv").show()
			$("#hideAdvancedButton").show()
			$("#showAdvancedButton").hide()
  		};

  		function hideAdvanced() {
			$("#advancedDiv").hide();
			$("#hideAdvancedButton").hide()
			$("#showAdvancedButton").show()
  		};

	</r:script>


    	<div id="showAdvancedButton" class="pull-right">
       		<a href="javascript:void(0);" onclick="showAdvanced();">Show Advanced Settings</a>
        </div>

    	<div id="hideAdvancedButton" class="pull-right">
       		<a href="javascript:void(0);" onclick="hideAdvanced();">Hide Advanced Settings</a>
        </div>

        <ui:field bean="instance" name="name"/>
    	<ui:field bean="instance" name="speciesSet" from="${acSpeciesSets}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
        <div id="advancedDiv">
			<ui:field bean="instance" name="periodicity"/>
			<ui:field bean="instance" name="repeatFromElement"/>
			<ui:field bean="instance" name="repetitions"/>
			<ui:field bean="instance" name="variablesReferenced"/>
        </div>