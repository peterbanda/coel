<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${nav.activePath()}">
	<g:set var="module">
		${StringUtils.substringBetween(nav.activePath(), '/')}
	</g:set>
	<g:if test="${module?.trim() == 'chemistry'}">
		<ui:image uri="/images/dna.png" width="100" title="Chemistry" alt="Chemistry"/>
	</g:if>
	<g:if test="${module?.trim() == 'network'}">
		<ui:image uri="/images/net.png" width="100" title="Network" alt="Network"/>
	</g:if>
	<g:if test="${module?.trim() == 'evolution'}">
		<ui:image uri="/images/evo.png" width="100" title="Evolution" alt="Evolution"/>
	</g:if>
</g:if>