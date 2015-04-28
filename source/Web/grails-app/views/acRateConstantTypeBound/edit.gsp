<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acRateConstantTypeBound.label', default: 'Rate Const. Bound')}" scope="request" />
    <nav:set path="app/chemistry/RateConstantTypeBound"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
        <f:display bean="instance" property="id"/>
        <f:ref property="createdBy" textProperty="username"/>
        <hr>
		<ui:field bean="instance" name="rateConstantType" required="${true}"/>
        <ui:field bean="instance" name="from"/>
        <ui:field bean="instance" name="to"/>
    </theme:zone>
</body>
</html>