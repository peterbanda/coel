<%@ page import="com.banda.math.domain.rand.RandomDistributionType"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${it != null}">
	<ui:field label="Type">
		<ui:fieldInput>
			${it.type}
		</ui:fieldInput>
	</ui:field>
	<g:if test="${it.getType() == RandomDistributionType.Uniform}">
		<ui:field label="From">
			<ui:fieldInput>
	            ${formatNumber(number: it.from, type: 'number', maxFractionDigits: 10)}                               
			</ui:fieldInput>
		</ui:field>
		<ui:field label="To">
			<ui:fieldInput>
	            ${formatNumber(number: it.to, type: 'number', maxFractionDigits: 10)}                               
			</ui:fieldInput>
		</ui:field>
	</g:if>
	<g:if test="${it.getType() == RandomDistributionType.Discrete}">
		<ui:field label="Value Type">
			<ui:fieldInput>
	            ${it.valueType.name}                               
			</ui:fieldInput>
		</ui:field>
		<ui:field label="Probabilities">
			<ui:fieldInput>
	            ${StringUtils.join(it.probabilities, ", ")}                           
			</ui:fieldInput>
		</ui:field>
		<ui:field label="Values">
			<ui:fieldInput>
	           ${StringUtils.join(it.values, ", ")}                      
			</ui:fieldInput>
		</ui:field>
	</g:if>
	<g:if test="${it.getType() == RandomDistributionType.BooleanDensityUniform}">
		<!-- Nothing to do? -->
	</g:if>
	<g:else>
		<ui:field label="Location">
			<ui:fieldInput>
	            ${formatNumber(number: it.location, type: 'number', maxFractionDigits: 10)}                               
			</ui:fieldInput>
		</ui:field>
		<ui:field label="Shape">
			<ui:fieldInput>
	            ${formatNumber(number: it.shape, type: 'number', maxFractionDigits: 10)}                               
			</ui:fieldInput>
		</ui:field>
	</g:else>
</g:if>