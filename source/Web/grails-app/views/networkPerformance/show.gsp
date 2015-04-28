<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkPerformance.label', default: 'Evaluated Performance')}" scope="request" />
    <nav:set path="app/network/Performance"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
            <hr>
			<f:ref property="network" textProperty="name"/>
			<f:ref property="evaluation" textProperty="name"/>
			<f:ref property="interactionSeries" textProperty="name"/>
			<f:display property="repetitions"/>
			<f:display property="runTime"/>
        </f:with>
        
        <hr>
        <g:render template="/stats/display" bean="${instance.result}"/>
    </theme:zone>
</body>
</html>