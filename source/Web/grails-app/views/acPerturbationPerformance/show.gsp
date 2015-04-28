<%@ page import="java.util.Arrays" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acPerturbationPerformance.label', default: 'Perturbation Performance')}" scope="request" />
    <nav:set path="app/chemistry/PerturbationPerformance"/>
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
					<f:ref property="compartment" textProperty="label"/>
					<f:ref property="simulationConfig" textProperty="name"/>
    			</div>
    			<div class="span5">
					<f:ref property="evaluation" textProperty="name"/>
					<f:ref property="actionSeries" textProperty="name"/>
					<f:display property="repetitions"/>
					<f:display property="length"/>
					<f:display property="perturbationNum"/>
					<f:display property="perturbationStrength"/>
					<f:display property="averagedCorrectRates">${Arrays.toString(value)}</f:display>
    			</div>
			</f:with>
		</div>
	</theme:zone>
</body>
</html>