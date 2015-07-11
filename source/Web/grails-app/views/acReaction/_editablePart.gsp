<%@ page import="com.banda.chemistry.domain.AcCollectiveSpeciesReactionAssociationType" %>
<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
<%@ page import="com.banda.chemistry.domain.AcSpeciesAssociationType" %>

    <r:script>

		$(function(){
			if (${!instance.label}) {
				$("#labelDiv").hide();
			}
			if (${!instance.hasSpeciesAssociations(AcSpeciesAssociationType.Catalyst)}) {
				$("#catalystsDiv").hide();
				$("#collectiveCatalysisTypeDiv").hide()
			} else if(${instance.getSpeciesAssociationsNum(AcSpeciesAssociationType.Catalyst) < 2}) {
				$("#collectiveCatalysisTypeDiv").hide()
			}

			if (${!instance.hasSpeciesAssociations(AcSpeciesAssociationType.Inhibitor)}) {
				$("#inhibitorsDiv").hide();
				$("#collectiveInhibitionTypeDiv").hide()
			} else if(${instance.getSpeciesAssociationsNum(AcSpeciesAssociationType.Inhibitor) < 2}) {
				$("#collectiveInhibitionTypeDiv").hide()
			}

			if (${!instance.hasReverseRateConstants()}) {
				$("#reverseRateConstantsDiv").hide();
			}
			if (${!instance.forwardRateFunction}) {
				$("#forwardRateFunctionDiv").hide();
			}
			if (${!instance.reverseRateFunction}) {
				$("#reverseRateFunctionDiv").hide();
			}

	    	$("#reactants").focus();
		});

  		function showAdvanced() {
			$("#labelDiv").show();
			$("#catalystsDiv").show();
			$("#inhibitorsDiv").show();
			$("#reverseRateConstantsDiv").show();
			$("#forwardRateFunctionDiv").show();
			$("#reverseRateFunctionDiv").show();
  		};

  		function checkCollectiveCatalysis() {
  			if ($("#catalystIds option:selected").length > 1) 
  				$("#collectiveCatalysisTypeDiv").show()
  			else
  				$("#collectiveCatalysisTypeDiv").hide()
  		};

  		function checkCollectiveInhibition() {
  			if ($("#inhibitorIds option:selected").length > 1) 
  				$("#collectiveInhibitionTypeDiv").show()
  			else
  				$("#collectiveInhibitionTypeDiv").hide()
  		};
	</r:script>

		<g:if test="${instance.reactionSet.groups}">
    		<ui:field bean="instance" name="group" from="${instance.reactionSet.groups}" required="${false}"/>
    	</g:if>

        <div class="pull-right">
       		<a href="javascript:void(0);" onclick="showAdvanced();">Show Advanced Settings</a>
        </div>

		<div id="labelDiv">
    		<ui:field bean="instance" name="label"/>
    	</div>

    	<ui:field name="acReactionSet.reaction">
    		<ui:fieldInput>
            	<g:textField name="reactants" value="${render(template:'displayReactants', bean:instance)}" />
                	&rarr;
                <g:textField name="products" value="${render(template:'displayProducts', bean:instance)}" />
        	</ui:fieldInput>
    	</ui:field>

		<div id="catalystsDiv">
	    	<ui:field label="Catalysts">
	    		<ui:fieldInput>
	    			<g:select name="catalystIds" from="${instance.reactionSet?.getSpecies()}"
    					optionValue="label"
    					optionKey="id"
    					value="${ArtificialChemistryUtil.getInstance().getSpecies(instance.getSpeciesAssociations(AcSpeciesAssociationType.Catalyst))}"
    					multiple="yes"
    					size="3"
    					onchange="checkCollectiveCatalysis();" />
            	</ui:fieldInput>
       		</ui:field>
       	</div>

		<div id="collectiveCatalysisTypeDiv">
	    	<ui:field bean="instance" name="collectiveCatalysisType"
    			from="${AcCollectiveSpeciesReactionAssociationType.values()}"
    			optionKey="name"
    			value="${instance?.collectiveCatalysisType?.name}"
    			required="${true}"/>
		</div>

		<div id="inhibitorsDiv">
		    <ui:field label="Inhibitors">
	    		<ui:fieldInput>
	    			<g:select name="inhibitorIds" from="${instance.reactionSet?.getSpecies()}"
    					from="${instance.reactionSet?.getSpecies()}"
    					optionValue="label"
    					optionKey="id"
    					value="${ArtificialChemistryUtil.getInstance().getSpecies(instance.getSpeciesAssociations(AcSpeciesAssociationType.Inhibitor))}"
    					multiple="yes"
    					size="3"
    					onchange="checkCollectiveInhibition();" />
	            </ui:fieldInput>
       		</ui:field>
		</div>

		<div id="collectiveInhibitionTypeDiv">
    		<ui:field bean="instance" name="collectiveInhibitionType"
    			from="${AcCollectiveSpeciesReactionAssociationType.values()}"
    			optionKey="name"
    			value="${instance?.collectiveInhibitionType?.name}"
    			required="${true}"/>
    	</div>

    	<ui:field bean="instance" name="forwardRateConstants" label="Forward Rate Constant(s)">
    		<ui:fieldInput>
            	<g:textField name="forwardRateConstants" value="${render(template:'displayForwardRateConstants', bean:instance)}" />
        	</ui:fieldInput>
    	</ui:field>

		<div id="forwardRateFunctionDiv">
			<ui:field bean="instance" name="forwardRateFunction">
				<ui:fieldInput>
					<g:textField name="forwardRateFunction.formula" value="${render(template:'displayForwardRateFunction', bean:instance)}" />
				</ui:fieldInput>
			</ui:field>
		</div>

		<div id="reverseRateConstantsDiv">
    		<ui:field bean="instance" name="reverseRateConstants" label="Reverse Rate Constant(s)">
    			<ui:fieldInput>
            		<g:textField name="reverseRateConstants" value="${render(template:'displayReverseRateConstants', bean:instance)}" />
        		</ui:fieldInput>
    		</ui:field>
    	</div>

		<div id="reverseRateFunctionDiv">
			<ui:field bean="instance" name="reverseRateFunction">
				<ui:fieldInput>
					<g:textField name="reverseRateFunction.formula" value="${render(template:'displayReverseRateFunction', bean:instance)}" />
				</ui:fieldInput>
			</ui:field>
		</div>