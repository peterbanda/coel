<%@ page import="com.banda.math.task.EvoRunTask" %>
<html>
<head>
    <g:set var="domainNameLabel" value="${message(code: 'task.label', default: 'Task')}" scope="request" />
    <nav:set path="app/evolution/Task"/>
    <theme:layout name="show"/>
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

			<hr>

			<f:display property="repeat"/>
			<f:display property="jobsInSequenceNum"/>
			<f:display property="maxJobsInParallelNum"/>

			<g:if test="${instance?.getClass() == EvoRunTask.class}">
				<f:display property="populationContentStoreOption"/>
				<f:display property="populationSelection"/>
				<f:display property="autoSave"/>
			</g:if>
		</f:with>
    </theme:zone>
</body>
</html>