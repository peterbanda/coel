<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'singleRunAnalysisResult.label', default: 'Single Analysis Result')}" scope="request" />
    <nav:set path="app/chemistry/SingleRunAnalysisResult"/>
    <theme:layout name="show"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
        	<f:display property="id"/>
			<f:display property="timeCreated"/>
            <hr>
			<f:ref property="multiRunResult"/>
        	<f:display property="initialState"/>
			<f:ref property="spatialCorrelations">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="timeCorrelations">${render(template:'displayStatsSeq', bean:it)}</f:ref>
	        <f:display property="neighborTimeCorrelations"/>
			<f:ref property="spatialStationaryPointsPerTime">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="timeStationaryPointsPerTime">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="spatialCumulativeDiffPerTime">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="timeCumulativeDiffPerTime">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="spatialNonlinearityErrors">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:ref property="timeNonlinearityErrors">${render(template:'displayStatsSeq', bean:it)}</f:ref>
			<f:display property="finalFixedPointsDetected"/>
			<f:display property="meanFixedPointsDetected"/>
			<f:display property="unboundValuesDetected"/>
			<f:display property="finalLyapunovExponents"/>
			<f:ref property="derridaResults">${render(template:'displayStatsSeq', bean:it)}</f:ref>
        </f:with>
    </theme:zone>
</body>
</html>