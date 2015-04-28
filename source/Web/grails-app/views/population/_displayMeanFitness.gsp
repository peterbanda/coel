<%@ page import="com.banda.math.business.evo.EvolutionUtil" %>
${formatNumber(number: EvolutionUtil.getMeanFitness(it), type: 'number', maxFractionDigits: 5)}
