<%@ page import="com.banda.math.task.EvoRunTask" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'task.label', default: 'Task')}" scope="request" />
    <nav:set path="app/evolution/Task"/>
    <theme:layout name="edit"/>
</head>
<body>
    <theme:zone name="details">
		<f:with bean="instance">
			<f:display property="id"/>
			<f:display property="timeCreated"/>
			<f:ref property="createdBy" textProperty="username"/>
			<g:if test="${instance?.getClass() == EvoRunTask.class}">
				<f:ref property="evoTask" textProperty="name"/>
			</g:if>		
		</f:with>

		<hr>

		<ui:field bean="instance" name="repeat"/>
		<ui:field bean="instance" name="jobsInSequenceNum"/>
		<ui:field bean="instance" name="maxJobsInParallelNum"/>

		<g:if test="${instance?.getClass() == EvoRunTask.class}">
			<ui:field bean="instance" name="populationContentStoreOption"/>
			<ui:field bean="instance" name="populationSelection"/>
			<ui:field bean="instance" name="autoSave"/>
		</g:if>
    </theme:zone>
</body>
</html>