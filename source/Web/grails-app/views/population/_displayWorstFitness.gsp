<%@ page import="com.banda.math.business.evo.EvolutionUtil" %>
${formatNumber(number: EvolutionUtil.getWorstFitness(it), type: 'number', maxFractionDigits: 5)}
