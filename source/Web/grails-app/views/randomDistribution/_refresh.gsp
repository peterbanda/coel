<%@ page import="com.banda.math.domain.rand.RandomDistributionType"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${randomDistribution}">
	<g:if test="${randomDistribution?.getType() == RandomDistributionType.Uniform}">
		<td valign="top" class="name">
			<label for="${prefix}FromTo"><g:message code="randomDistribution.fromTo.label" default="From/To" /></label>
		</td>
		<td valign="top" class="value">
			<g:textField name="${prefix}From" value="${randomDistribution?.from}" />
			<g:textField name="${prefix}To" value="${randomDistribution?.to}" />
		</td>
	</g:if>
	<g:if test="${randomDistribution?.getType() == RandomDistributionType.Discrete}">
		<td valign="top" class="name">
			<label for="${prefix}ProbabilitiesValues"><g:message code="randomDistribution.probabilitiesValues.label" default="Probabilities/Values" /></label>
		</td>
		<td valign="top" class="value">
			<g:select name="${prefix}ValueTypeName" from="${['java.lang.Boolean', 'Boolean', 'java.lang.Double', 'Double']}"/>
			<g:if test="${randomDistribution.probabilities}">
				<g:textField name="${prefix}Probabilities" value="${StringUtils.join(randomDistribution.probabilities, ", ")}" />
			</g:if>
			<g:else>
				<g:textField name="${prefix}Probabilities" value="" />
			</g:else>
			<g:if test="${randomDistribution.values}">
				<g:textField name="${prefix}Values" value="${StringUtils.join(randomDistribution.values, ", ")}" />
			</g:if>
			<g:else>
				<g:textField name="${prefix}Values" value="" />
			</g:else>
		</td>
	</g:if>
	<g:if test="${randomDistribution?.getType() == RandomDistributionType.BooleanDensityUniform}">
		<!-- Nothing to do? -->
	</g:if>
	<g:else>
		<td valign="top" class="name">
			<label for="${prefix}LocationShape"><g:message code="randomDistribution.locationShape.label" default="Location/Shape" /></label>
		</td>
		<td valign="top" class="value">
			<g:textField name="${prefix}Location" value="${randomDistribution?.location}" />
			<g:textField name="${prefix}Shape" value="${randomDistribution?.shape}" />
		</td>
	</g:else>
</g:if>