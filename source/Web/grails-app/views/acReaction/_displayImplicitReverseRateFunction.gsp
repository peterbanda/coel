<%@ page import="com.banda.chemistry.business.AcKineticsBO" %>
<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
${String.format(
	ArtificialChemistryUtil.getInstance().getFunctionAsString(
		AcKineticsBO.createInstance(it, false).createRateFunctionWithoutConstants(),
		it.getReactionSet()),
	it.getReverseRateConstants().collect{ rateConstant -> String.format("%.4f", rateConstant)}.toArray())}