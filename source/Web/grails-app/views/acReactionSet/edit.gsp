<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acReactionSet.label', default: 'Reaction Set')}" scope="request" />
    <nav:set path="app/chemistry/ReactionSet"/>
    <r:require module="editable"/>
    <theme:layout name="edit"/>
</head>
<body>
	<theme:zone name="extras">
		<g:render template="extras"/>
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
					<f:ref property="speciesSet" textProperty="name"/>
					<g:if test="${!instance?.speciesSet.parameterSet.variables.isEmpty()}">
						<f:ref property="speciesSet.parameterSet" textProperty="name"/>
					</g:if>
					<f:display label="Reaction #" property="id">
						${instance.reactions.size()}
					</f:display>
    			</div>
			</f:with>    		
		</div>
		<hr/>
		<div class="spacedLeft">			
			<g:render template="species"/>	
		</div>
        <div class="accordion" id="accordion">
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" href="#collapseOne">Reactions</a>
    			</div>
    			<div id="collapseOne" class="accordion-body collapse in">
      				<div class="accordion-inner">
						<g:render template="reactions"/>
      				</div>
      			</div>
    		</div>
  			<div class="accordion-group">
    			<div class="accordion-heading">
        			<a class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">Reaction Groups</a>
    			</div>
    			<div id="collapseTwo" class="accordion-body collapse in">
      				<div class="accordion-inner">
						<g:render template="reactionGroups"/>
      				</div>
      			</div>
    		</div>
  		</div>
    </theme:zone>
</body>
</html>