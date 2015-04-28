<%@ page import="com.banda.chemistry.domain.AcMultiRunAnalysisResult" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'multiRunAnalysisResult.label', default: 'Multi Analysis Result')}" scope="request" />
    <nav:set path="app/chemistry/MultiRunAnalysisResult"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>

			<hr>

			<f:ref property="spec" textProperty="id"/>
			<g:if test="${instance?.getClass() == AcMultiRunAnalysisResult.class}">
				<f:ref property="ac" textProperty="name"/>
	        </g:if>
			
			<ui:field bean="instance" name="singleRunResults">
				<ui:fieldInput>
					<div class="row-fluid span4">
						<gui:table domainName="singleRunAnalysisResult" list="${instance.singleRunResults}" showEnabled="true">
       						<gui:column property="timeCreated"/>
							<gui:column label="spatialCorrelations">
								<g:render template="/singleRunAnalysisResult/displayStatsSeq" bean="${it}" />
							</gui:column>
							<gui:column label="timeCorrelations">
								<g:render template="/singleRunAnalysisResult/displayStatsSeq" bean="${it}" />
							</gui:column>
							<gui:column label="spatialStationaryPointsPerTime">
								<g:render template="/singleRunAnalysisResult/displayStatsSeq" bean="${it}" />
							</gui:column>
							<gui:column label="timeStationaryPointsPerTime">
								<g:render template="/singleRunAnalysisResult/displayStatsSeq" bean="${it}" />
							</gui:column>							
						</gui:table>
					</div>
				</ui:fieldInput>
			</ui:field>
		</f:with>
    </theme:zone>
</body>
</html>