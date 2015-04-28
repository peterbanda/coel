<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
<%@ page import="com.banda.function.BndFunctionException" %>

${it.species.label} &larr;
	<%  
	try {
		def string = ArtificialChemistryUtil.getInstance().getSettingFunctionAsString(it);
		out << string
	} catch (BndFunctionException e) {
		out << "Error : " + e.getMessage()
	}
	%>