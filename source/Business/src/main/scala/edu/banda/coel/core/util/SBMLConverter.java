package edu.banda.coel.core.util;

import java.util.*;
import java.util.Map.Entry;
import java.util.Date;

import org.sbml.libsbml.*;

import com.banda.chemistry.domain.*;
import com.banda.core.EntryKeyLengthDescComparator;
import com.banda.function.business.FunctionUtility;
import com.banda.function.domain.Expression;
import com.banda.function.domain.Function;
import com.banda.function.domain.FunctionHolder;
import com.banda.function.domain.ODESolverType;

import edu.banda.coel.CoelRuntimeException;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class SBMLConverter {

	private static SBMLConverter instance = new SBMLConverter();

	final static String[] VAR_PRECEDING_STRING = new String[] {" ", ",", "\\(","\\*","-","/","\\^"};
	final static String[] VAR_NEXT_STRING = new String[] {" ", ",", "\\)","\\*","-","/","\\^"};
	final static String[] FUN_OPER_PART = new String[] {"(", ")", "+","*","-","/","^"};
	private final FunctionUtility functionUtility = new FunctionUtility();

	private SBMLConverter() {
		// empty constructor
		checkRequiredLibs();
	}

	public static SBMLConverter getInstance() {
		return instance;
	}

	private void checkRequiredLibs() {
		try {
			System.loadLibrary("sbmlj");
			/* Extra check to be sure we have access to libSBML: */
			Class.forName("org.sbml.libsbml.libsbml");
		} catch (Exception e) {
			throw new CoelRuntimeException("Error: could not load the libSBML library", e);
		}
	}

	private SBMLDocument getSBMLDocumentFromFile(String fileName) {
		SBMLReader reader = new SBMLReader();
		SBMLDocument document = reader.readSBML(fileName);

		if (document.getNumErrors() > 0) {
			document.printErrors();
			throw new CoelRuntimeException("Errors found in SBML model " + fileName);
		}
		return document;
	}

	private SBMLDocument getSBMLDocumentFromString(String content) {
		SBMLReader reader = new SBMLReader();
		SBMLDocument document = reader.readSBMLFromString(content);

		if (document.getNumErrors() > 0) {
			document.printErrors();
			throw new CoelRuntimeException("Errors found in SBML model " + content);
		}
		return document;
	}

	public String convertSBMLFileToString(String fileName) {
		SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
		return sbmlDocument.toSBML();
	}

	public ArtificialChemistry convertSBMLStringToAlChemistry(String sbmlString) {
		SBMLDocument sbmlDocument = getSBMLDocumentFromString(sbmlString);
		return convertSBMLToAlChemistry(sbmlDocument);
	}

	public ArtificialChemistry convertSBMLFileToAlChemistry(String fileName) {
		SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
		return convertSBMLToAlChemistry(sbmlDocument);
	}

	private ArtificialChemistry convertSBMLToAlChemistry(SBMLDocument sbmlDocument) {
		Model sbmlModel = sbmlDocument.getModel();

		ListOfCompartments compartments = sbmlModel.getListOfCompartments();
		ListOfSpecies species = sbmlModel.getListOfSpecies();
		ListOfReactions reactions = sbmlModel.getListOfReactions();
		ListOfParameters parameters = sbmlModel.getListOfParameters();
		ListOfRules rules = sbmlModel.getListOfRules();
		int speciesNum = (int) species.size();

		ArtificialChemistry alChemistry = new ArtificialChemistry();
		alChemistry.setName("SBML Model " + new Date().getTime());
		alChemistry.setCreateTime(new Date());

		AcSimulationConfig simConfig = new AcSimulationConfig();
		simConfig.setOdeSolverType(ODESolverType.RungeKutta4);
		alChemistry.setSimulationConfig(simConfig);

		Map<String, AcCompartment> labelCompartmentMap = new HashMap<String, AcCompartment>();
		AcCompartment skinCompartment = null;
		for (int compartmentId = 0; compartmentId < compartments.size(); compartmentId++) {
			Compartment compartment = compartments.get(compartmentId);
			AcCompartment acCompartment = new AcCompartment();
			acCompartment.setLabel(compartment.getId());
			AcReactionSet reactionSet = new AcReactionSet();

			AcSpeciesSet speciesSet = new AcSpeciesSet();
			AcParameterSet parameterSet = new AcParameterSet();
			speciesSet.setParameterSet(parameterSet);
			parameterSet.setSpeciesSet(speciesSet);

			reactionSet.setSpeciesSet(speciesSet);
			reactionSet.setLabel("SBML Imported" + new Date().getTime());
			acCompartment.setReactionSet(reactionSet);

			labelCompartmentMap.put(acCompartment.getLabel(), acCompartment);
			// TODO - support hierarchical compartments
			skinCompartment = acCompartment;
		}
		alChemistry.setSkinCompartment(skinCompartment);

		Map<String, AcSpecies> labelSpeciesStructureMap = new HashMap<String, AcSpecies>();
		for (int speciesIndex = 0; speciesIndex < speciesNum; speciesIndex++) {
			Species oneSpecies = species.get(speciesIndex);

			AcSpecies acSpecies = new AcSpecies();
			acSpecies.setVariableIndex(speciesIndex);
			acSpecies.setLabel(oneSpecies.getId());
			acSpecies.setSortOrder(speciesIndex);

			AcCompartment associatedCompartment = labelCompartmentMap.get(oneSpecies.getCompartment());
			associatedCompartment.getReactionSet().getSpeciesSet().addVariable(acSpecies);
			labelSpeciesStructureMap.put(acSpecies.getLabel(), acSpecies);
		}

		Map<String, AcParameter> labelParameterMap = new HashMap<String, AcParameter>();
		for (int parameterIndex = 0; parameterIndex < parameters.size(); parameterIndex++) {
			Parameter parameter = parameters.get(parameterIndex);
			AcParameter acParameter = new AcParameter();
			acParameter.setVariableIndex(speciesNum + parameterIndex);
			acParameter.setLabel(parameter.getId());
			acParameter.setSortOrder(parameterIndex);
			acParameter.setEvolFunction(Expression.Double(String.valueOf(parameter.getValue())));
			labelParameterMap.put(acParameter.getLabel(), acParameter);
			skinCompartment.getReactionSet().getParameterSet().addVariable(acParameter);
		}

		Map<String, AcVariable> labelMagnitudeMap = new HashMap<String, AcVariable>();
		labelMagnitudeMap.putAll(labelSpeciesStructureMap);
		labelMagnitudeMap.putAll(labelParameterMap);

		for (int reactionIndex = 0; reactionIndex < reactions.size(); reactionIndex++) {
			Reaction reaction = reactions.get(reactionIndex);
			AcReaction acReaction = new AcReaction();
			acReaction.setIndex(reactionIndex); // not needed?
			acReaction.setLabel(reaction.getId());
			acReaction.setSortOrder(reactionIndex);

			Set<AcSpeciesReactionAssociation> reactants = getSpeciesAssociations(speciesNum, reaction.getListOfReactants(), labelSpeciesStructureMap);
			Set<AcSpeciesReactionAssociation> products = getSpeciesAssociations(speciesNum, reaction.getListOfProducts(), labelSpeciesStructureMap);

			acReaction.addSpeciesAssociations(reactants, AcSpeciesAssociationType.Reactant);
			acReaction.addSpeciesAssociations(products, AcSpeciesAssociationType.Product);

			KineticLaw kinetricLaw = reaction.getKineticLaw();
			ASTNode astNode = kinetricLaw.getMath();
			setFunction(acReaction, astNode, labelMagnitudeMap);

			skinCompartment.getReactionSet().addReaction(acReaction);
		}

		for (int ruleId = 0; ruleId < rules.size(); ruleId++) {
			Rule rule = rules.get(ruleId);
			AcParameter acParameter = labelParameterMap.get(rule.getVariable());
			ASTNode astNode = rule.getMath();
			setFunction(acParameter, astNode, labelMagnitudeMap);
		}
		return alChemistry;
	}

	private Double[] getStoichiometricVector(
		int speciesNum,
		ListOfSpeciesReferences speciesReferences,
		Map<String, AcSpecies> labelSpeciesMap
	) {
		Double[] stoichiometricVector = new Double[speciesNum];
		for (int i = 0; i < speciesNum; i++) {
			stoichiometricVector[i] = new Double(0);
		}
		for (int reactantId = 0; reactantId < speciesReferences.size(); reactantId++) {
			SimpleSpeciesReference reactantReference = speciesReferences.get(reactantId);
			AcSpecies acSpecies = labelSpeciesMap.get(reactantReference.getSpecies());
			double stoichiometry = 1.0;
			if (reactantReference instanceof SpeciesReference) {
				stoichiometry = ((SpeciesReference) reactantReference).getStoichiometry();
			}
			stoichiometricVector[acSpecies.getVariableIndex()] = stoichiometry;
		}
		return stoichiometricVector;
	}

	private Set<AcSpeciesReactionAssociation> getSpeciesAssociations(
		int speciesNum,
		ListOfSpeciesReferences speciesReferences,
		Map<String, AcSpecies> labelSpeciesMap
	) {
		Set<AcSpeciesReactionAssociation> speciesAssociations = new HashSet<AcSpeciesReactionAssociation>();
		for (int reactantId = 0; reactantId < speciesReferences.size(); reactantId++) {
			SimpleSpeciesReference reactantReference = speciesReferences.get(reactantId);
			AcSpecies acSpecies = labelSpeciesMap.get(reactantReference.getSpecies());
			double stoichiometry = 1.0;
			if (reactantReference instanceof SpeciesReference) {
				stoichiometry = ((SpeciesReference) reactantReference).getStoichiometry();
			}
			speciesAssociations.add(new AcSpeciesReactionAssociation(acSpecies, stoichiometry));
		}
		return speciesAssociations;
	}

	private void setFunction(
		FunctionHolder<Double, Double> functionHolder,
		ASTNode astNode,
		Map<String, AcVariable> labelMagnitudeMap
	) {
		functionHolder.setFunction(getExpression(astNode, labelMagnitudeMap));
	}

	private Function<Double, Double> getExpression(
		ASTNode astNode,
		Map<String, AcVariable> labelMagnitudeMap
	) {
		String formula = libsbml.formulaToString(astNode);

		formula = replaceVariables(formula, labelMagnitudeMap);
		formula = replaceBinaryFunctionByOperator("pow", "^", formula);
		formula = replacePiecewise(formula);

		return Expression.Double(formula);
	}

	private String replaceVariables(String formula, Map<String, AcVariable> labelMagnitudeMap) {
		List<Entry<String, AcVariable>> sortedEntries = new ArrayList<Entry<String, AcVariable>>();
		sortedEntries.addAll(labelMagnitudeMap.entrySet());
		Collections.sort(sortedEntries, new EntryKeyLengthDescComparator<AcVariable>());

		for (Entry<String, AcVariable> entry : sortedEntries) {
//			String stringToReplace = precedingChar + entry.getKey() + nextChar;
//			String replacementString = precedingChar + "x" + entry.getValue().getVariableIndex() + nextChar;
			formula = formula.replaceAll(entry.getKey(), functionUtility.getVariablePlaceHolder(entry.getValue().getVariableIndex()));				
		}
		return formula;
	}

	private String replacePiecewise(String formula) {
		String[] tokens = formula.split("piecewise\\(");
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String token : tokens) {
			if (first) {
				first = false;
				sb.append(token);
			} else {
				int fistCommaIndex = token.indexOf(",");
				int secondCommaIndex = token.indexOf(",", fistCommaIndex + 1);
				int thridCommaIndex = token.indexOf(",", secondCommaIndex + 1);
				int rightBracketIndex = token.indexOf(")", thridCommaIndex + 1);
				String firstValue = token.substring(0, fistCommaIndex).trim();
				String condition = token.substring(fistCommaIndex + 1, thridCommaIndex).trim();
				String secondValue = token.substring(thridCommaIndex + 1, rightBracketIndex).trim();
				String rest = "";
				if (rightBracketIndex < token.length() - 1) {
					rest = token.substring(rightBracketIndex + 1);
				}
				sb.append("if(");
				condition = replaceBinaryFunctionByOperator("lt", " < ", condition);
				condition = replaceBinaryFunctionByOperator("gt", " > ", condition);
				condition = replaceBinaryFunctionByOperator("eq", " = ", condition);
				sb.append(condition);
				sb.append(",");
				sb.append(firstValue);
				sb.append(",");
				sb.append(secondValue);
				sb.append(")");
				sb.append(rest);
			}
		}
		return sb.toString();
	}

	private String replaceBinaryFunctionByOperator(String functionName, String operatorName, String formula) {
		String[] tokens = formula.split(functionName + "\\(");
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String token : tokens) {
			if (first) {
				first = false;
				sb.append(token);
			} else {
				int commaIndex = token.indexOf(",");
				if (commaIndex == -1) {
					throw new CoelRuntimeException(functionName + " function defined as '" + token + "' in " + formula + " is not valid.");
				}
				int rightBracketIndex = token.indexOf(")", commaIndex + 1);
				String base = token.substring(0, commaIndex).trim();
				String exponent = token.substring(commaIndex + 1, rightBracketIndex).trim();
				String rest = "";
				if (rightBracketIndex < token.length() - 1) {
					rest = token.substring(rightBracketIndex + 1);
				}
				boolean compositeFlag = false;
				for (String funOperPart : FUN_OPER_PART) {
					if (compositeFlag = base.indexOf(funOperPart) != -1) {
						break;
					}
				}
				if (compositeFlag) {
					sb.append("(");
				}
				sb.append(base);
				if (compositeFlag) {
					sb.append(")");
				}
				sb.append(operatorName);
				sb.append(exponent);
				sb.append(rest);
			}
		}
		return sb.toString();
	}

	public static void main(String args[]) {
		SBMLConverter sbmlConverter = SBMLConverter.getInstance();
		ArtificialChemistry alChemistry = sbmlConverter.convertSBMLFileToAlChemistry("perceptron_v06.xml");
		System.out.println(alChemistry);
	}
}