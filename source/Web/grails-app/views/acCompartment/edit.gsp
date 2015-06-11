<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acCompartment.label', default: 'Compartment')}" scope="request" />
    <nav:set path="app/chemistry/Compartment"/>
    <r:require module="jquery-ui"/>
    <theme:layout name="edit"/>
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
				<li role="presentation"><a role="menuitem" href="${createLink(controller:"acInteractionSeries", action: 'create', params:['speciesSet.id':instance.reactionSet.speciesSet.id])}">Create Interaction Series</a></li>
				<li role="presentation" class="divider"></li>
    			<li role="presentation"><a role="menuitem" href="javascript:void(0);" onclick="showImportRatesDialog();">Import rates</a></li>
  			</ul>
		</div>
    </theme:zone>

    <theme:zone name="details">
		<div class="row-fluid">
    	    <f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="createTime"/>
        			<f:ref property="createdBy" textProperty="username"/>
    			</div>
    			<div class="span5">
					<ui:field bean="instance" name="label"/>
					<ui:field bean="instance" name="reactionSet" from="${acReactionSets}" optionValue="${{it.id + ' : ' + it.label}}" required="${true}"/>
    			</div>
			</f:with>    		
		</div>

		<hr>

		<g:render template="parentCompartments"/>
		<g:render template="subCompartments"/>
		<g:render template="channels"/>
		<g:render template="subChannelGroups"/>

    </theme:zone>
</body>
</html>