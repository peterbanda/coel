<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteractionSeries.label', default: 'Interaction Series')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>);
    <r:require module="editable"/>
    <r:require module="select"/>
    <theme:layout name="edit"/>
    <r:script>
    	$(document).ready(function(){
    		$("#immutableSpecies").attr("data-selected-text-format", "count>5");
    		$('#immutableSpecies').selectpicker({
				dropupAuto: false
			});  
	   	});
	</r:script>
</head>
<body>
	<theme:zone name="extras">
	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showCopyDialog();"><g:message code="default.copysimple.label" args="[domainNameLabel]" /></a></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showReplaceSpeciesSetDialog();">Replace Species Set</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
       	<div class="row-fluid">
			<f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="timeCreated"/>
        			<f:ref property="createdBy" textProperty="username"/>
    			</div>
    			<div class="span7">
    				<g:render template="editablePart"/>
    				<ui:field bean="instance" name="immutableSpecies">
						<ui:fieldInput>			
							<g:select name="immutableSpecies" from="${instance.speciesSet.getOwnAndInheritedVariables()}" multiple="yes"
								optionKey="id"
								optionValue="label"
								size="5" 
                       			value="${instance?.immutableSpecies}" />
						</ui:fieldInput>
					</ui:field>
    			</div>
			</f:with>
		</div>
		<hr/>
			<div class="spacedLeft">
				<g:render template="variables"/>
			</div>
		<hr/>

        <div class="accordion" id="accordion">
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Interactions</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
						<g:render template="actions"/>
      				</div>
      			</div>
    		</div>
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Sub Interaction Series</a>
    			</div>
    			<div id="collapseTwo" class="accordion-body collapse in">
      				<div class="accordion-inner">
    					<g:render template="subActionSeries"/>
      				</div>
      			</div>
    		</div>
  		</div>
    </theme:zone>
</body>
</html>