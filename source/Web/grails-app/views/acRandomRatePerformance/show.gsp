<%@ page import="java.util.Arrays" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'acRandomRatePerformance.label', default: 'Random Rate Performance')}" scope="request" />
    <nav:set path="app/chemistry/RandomRatePerformance"/>
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
		            <f:display property="randomRateGenerationNum"/>
        		    <f:display property="averagedCorrectRates">${Arrays.toString(value)}</f:display>
    			</div>
			</f:with>
		</div>

		<ui:field bean="instance" name="rateConstantTypeBounds">
			<ui:fieldInput>
				<div class="row-fluid span4">
					<gui:table list="${instance.rateConstantTypeBounds}">
       					<gui:column property="rateConstantType"/>
						<gui:column label="From">${bean.bound.from}</gui:column>
						<gui:column label="To">${bean.bound.to}</gui:column>
					</gui:table>
				</div>
			</ui:fieldInput>
		</ui:field>

	</theme:zone>
</body>
</html>