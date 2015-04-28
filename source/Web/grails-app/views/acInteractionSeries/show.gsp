<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acInteractionSeries.label', default: 'Interaction Series')}" scope="request" />
    <nav:set path="app/chemistry/InteractionSeries"/>
    <r:require module="editable"/>
    <theme:layout name="show"/>
</head>
<body>
	<theme:zone name="actions">
		<gui:actionButton action="list" hint="List"/>
		<gui:actionButton action="create" hint="Add New"/>
		<gui:actionButton action="edit" id="${instance?.id}" hint="Edit"/>
		<gui:actionButton action="delete" id="${instance?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" hint="Delete"/>
		<gui:actionButton action="copy" hint="Copy" onclick="showCopyDialog();return false;"/>
		<g:if test="${SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')}">
			<gui:actionButton action="showPrevious" id="${instance?.id}" hint="Show Previous"/>
			<gui:actionButton action="showNext" id="${instance?.id}" hint="Show Next"/>
		</g:if>
	</theme:zone>

	<theme:zone name="extras">
	    <g:render template="dialogs_show"/>
        <div class="dropdown">
  			<ui:button kind="button" class="btn dropdown-toggle sr-only" id="dropdownMenu1" data-toggle="dropdown">
    			Extras
    			<span class="caret"></span>
  			</ui:button>
  			<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showReplaceSpeciesSetDialog();">Replace Species Set</a></li>
				<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showAddInteractionsDialog();">Add Interactions</a></li>
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
    			    <f:display property="name"/>
   					<g:if test="${instance?.parent}">
   						<f:ref property="parent">${it.id + ' : ' + it.name}</f:ref>
      				</g:if>
					<f:ref property="speciesSet" textProperty="name"/>
    				<f:display property="periodicity"/>
    				<f:display property="repeatFromElement"/>
    				<g:if test="${instance?.repetitions}">
    					<f:display property="repetitions"/>
    				</g:if>
    				<g:if test="${!instance?.immutableSpecies.isEmpty()}">
    				    <f:display property="immutableSpecies">
      						<ul class="inline">
      							<g:each in="${instance.immutableSpecies}" var="species" >
    	  							<li>
    	  								${species.label}
      								</li>
								</g:each>
							</ul>
						</f:display>
					</g:if>
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