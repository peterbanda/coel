<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acEvaluation.label', default: 'Evaluation')}" scope="request" />
    <nav:set path="app/chemistry/Evaluation"/>
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
    		        <f:ref property="translationSeries" textProperty="name"/>
	        		<f:display property="evalFunction"><g:render template="displayEvaluationFunction" bean="${instance}"/></f:display>
            		<f:display property="periodicTranslationsNumber"/>
    			</div>
			</f:with>    		
		</div>
    </theme:zone>
</body>
</html>