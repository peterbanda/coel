<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'networkEvaluation.label', default: 'Evaluation')}" scope="request" />
    <nav:set path="app/network/Evaluation"/>
    <theme:layout name="create"/>
</head>
<body>
    <theme:zone name="details">
        <ui:field bean="instance" name="name"/>
        <ui:field bean="instance" name="evalFunction">
        	<ui:fieldInput>
            	<g:textArea class="input-xxlarge" rows="4" name="evalFunction.formula" value="" />
        	</ui:fieldInput>
        </ui:field>
    </theme:zone>
</body>
</html>