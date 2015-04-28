<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acTranslationSeries.label', default: 'Translation Series')}" scope="request" />
    <nav:set path="app/chemistry/TranslationSeries"/>
    <r:require module="editable"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
       	<div class="row-fluid">
			<f:with bean="instance">
    			<div class="span5">
    				<f:display property="id"/>
        			<f:display property="timeCreated"/>
        			<f:ref property="createdBy" textProperty="username"/>
        			<f:display property="name"/>
    			</div>
    			<div class="span7">
					<f:ref property="speciesSet" textProperty="name"/>
    				<f:display property="periodicity"/>
    				<f:display property="repeatFromElement"/>
    				<f:display property="repetitions"/>
        			<f:display property="variablesReferenced"/>
    			</div>
			</f:with>
		</div>
		<hr/>
		<div class="spacedLeft">
			<g:render template="variables"/>
		</div>
		<hr/>
		<div class="spacedLeft">
        	<g:render template="translations"/>
        </div>
	</theme:zone>
</body>
</html>