<%@ page import="com.banda.function.domain.TransitionTable" %>
<%@ page import="com.banda.function.domain.Expression" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${it?.getClass() == TransitionTable.class}">
${StringUtils.join(it.outputs.collect{ output -> if (output) 1 else 0}, ", ")}
</g:if>
<g:if test="${it?.getClass() == Expression.class}">
${it.formula}
</g:if>