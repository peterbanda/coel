<%@ page import="org.apache.commons.lang.StringUtils" %>
${(it.hasRateConstants(false)) ? StringUtils.join(it.getRateConstants(false), ", ") : ""}
