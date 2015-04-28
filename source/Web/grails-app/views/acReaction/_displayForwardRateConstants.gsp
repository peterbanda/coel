<%@ page import="org.apache.commons.lang.StringUtils" %>
${(it.hasRateConstants(true)) ? StringUtils.join(it.getRateConstants(true), ", ") : ""}