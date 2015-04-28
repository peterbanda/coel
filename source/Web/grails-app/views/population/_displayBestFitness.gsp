<%@ page import="com.banda.math.business.evo.EvolutionUtil" %>
${formatNumber(number: EvolutionUtil.getBestFitness(it), type: 'number', maxFractionDigits: 5)}
