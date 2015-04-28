<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acSpeciesSet.label', default: 'Species Set')}" scope="request" />
    <nav:set path="app/chemistry/SpeciesSet"/>
    <r:require module="editable"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
       	<div class="row-fluid">
			<f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="createTime"/>
        			<f:ref property="createdBy" textProperty="username"/>
    			</div>
    			<div class="span5">
    				<f:display property="name"/>
					<f:ref property="parentSpeciesSet">${it.name + '/' + it.variables.size()}</f:ref>
					<f:ref property="parameterSet">${it.name + '/' + it.variables.size()}</f:ref>
					<f:display label="Species #" property="id">
						${instance.variables.size()}
					</f:display>    
    			</div>
			</f:with>
		</div>
		<hr/>
		<g:render template="species"/>
    </theme:zone>
</body>
</html>