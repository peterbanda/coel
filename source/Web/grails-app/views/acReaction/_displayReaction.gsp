<%@ page import="com.banda.chemistry.business.ArtificialChemistryUtil" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
${StringUtils.join(ArtificialChemistryUtil.getInstance().getReactantsAsString(it), " + ")}
&rarr;
${StringUtils.join(ArtificialChemistryUtil.getInstance().getProductsAsString(it), " + ")}
