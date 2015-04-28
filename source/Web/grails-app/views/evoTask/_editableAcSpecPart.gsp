<%@ page import="com.banda.chemistry.domain.ArtificialChemistrySpecBound" %>
<%@ page import="com.banda.chemistry.domain.AcSimulationConfig" %>
<%@ page import="com.banda.math.domain.dynamics.MultiRunAnalysisSpec" %>

	<ui:field bean="instance" name="taskType"/>
	<ui:field bean="instance" name="acSpecBound"
		from="${ArtificialChemistrySpecBound.list(sort: 'id')}"
		optionValue="${{it.id + " : " + it.name}}" required="${true}" />

	<ui:field bean="instance" name="simConfig"
		from="${AcSimulationConfig.list(sort: 'id')}"
		optionValue="${{it.id + " : " + it.name}}" required="${true}" />

	<ui:field bean="instance" name="multiRunAnalysisSpec"
		from="${MultiRunAnalysisSpec.list(sort: 'id')}"
		optionValue="${{it.id + " : " + it.name}}" required="${true}" />
