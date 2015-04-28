<%@ page import="com.banda.function.business.FunctionUtility" %>
<%
	def map = [0 : 'firstState', 1 : 'lastButOneState', 2 : 'lastState']
	def string = new FunctionUtility().getFormulaIndexPlaceholdersReplacedWithVariableNames(it.evalFunction, map)
	out << string
%>