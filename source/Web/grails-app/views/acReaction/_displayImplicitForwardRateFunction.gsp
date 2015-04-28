<%@ page import="com.banda.chemistry.business.AcKineticsBO" %>
<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
${String.format(
	ArtificialChemistryUtil.getInstance().getFunctionAsString(
		AcKineticsBO.createInstance(it, true).createRateFunctionWithoutConstants(),
		it.getReactionSet()),
	it.getForwardRateConstants().collect{ rateConstant -> String.format("%.4f", rateConstant)}.toArray())}