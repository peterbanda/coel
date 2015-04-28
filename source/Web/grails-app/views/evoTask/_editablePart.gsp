<%@ page import="edu.banda.coel.domain.evo.EvoAcRateConstantTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoAcSpecTask" %>
<%@ page import="edu.banda.coel.domain.evo.EvoNetworkTask" %>

		<g:hiddenField name="type" value="${instance.taskType}" />

		<ui:field bean="instance" name="name"/>
		<ui:field bean="instance" name="gaSetting" from="${gaSettings}" class="span6" optionValue="${{it.id + ' : ' + it.name}}" required="${true}"/>

		<hr>

		<g:if test="${instance?.getClass() == EvoAcRateConstantTask.class}">
			<g:render template="editableAcRateConstantPart"/>
		</g:if>

		<g:if test="${instance?.getClass() == EvoAcInteractionSeriesTask.class}">
			<g:render template="editableAcInteractionSeriesPart"/>
		</g:if>

		<g:if test="${instance?.getClass() == EvoAcSpecTask.class}">
			<g:render template="editableAcSpecPart"/>
		</g:if>

		<g:if test="${instance?.getClass() == EvoNetworkTask.class}">
			<g:render template="editableNetworkPart"/>
		</g:if>
