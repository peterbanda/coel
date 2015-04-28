<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acRateConstantTypeBound.label', default: 'Rate Const. Bound')}" scope="request" />
    <nav:set path="app/chemistry/RateConstantTypeBound"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
        	<f:ref property="createdBy" textProperty="username"/>
            <hr>
			<f:display property="rateConstantType"/>
            <f:display property="from"/>
            <f:display property="to"/>
        </f:with>
    </theme:zone>
</body>
</html>