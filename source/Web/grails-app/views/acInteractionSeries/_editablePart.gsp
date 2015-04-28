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
		<g:if test="${acSpeciesSets}">
    		<ui:field bean="instance" name="speciesSet" from="${acSpeciesSets}" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>
    	</g:if>
        <div id="advancedDiv">
        	<g:if test="${acInteractionSeries}">
    			<ui:field bean="instance" name="parent" from="${acInteractionSeries}" optionValue="${{it.id + ' : ' + it.name}}" required="${false}"/>
    		</g:if>
			<ui:field bean="instance" name="periodicity"/>
			<ui:field bean="instance" name="repeatFromElement"/>
			<ui:field bean="instance" name="repetitions"/>
		</div>