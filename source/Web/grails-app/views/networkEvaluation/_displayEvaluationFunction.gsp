<%@ page import="com.banda.function.business.FunctionUtility" %>
<%
	def map = [0 : 'firstState', 2 : 'lastButOneState', 4 : 'lastState']
	it.variables.each{ map.put(2 * it.variableIndex + 1, it.label) }
	def string = new FunctionUtility().getFormulaIndexPlaceholdersReplacedWithVariableNames(it.evalFunction, map)
	out << string
%>