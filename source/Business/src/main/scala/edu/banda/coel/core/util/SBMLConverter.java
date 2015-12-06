package edu.banda.coel.core.util;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Date;

import org.sbml.jsbml.*;

import com.banda.chemistry.domain.*;
import com.banda.core.EntryKeyLengthDescComparator;
import com.banda.function.business.FunctionUtility;
import com.banda.function.domain.Expression;
import com.banda.function.domain.Function;
import com.banda.function.domain.FunctionHolder;
import com.banda.function.domain.ODESolverType;

import edu.banda.coel.CoelRuntimeException;

import javax.xml.stream.XMLStreamException;

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
//		checkRequiredLibs();
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
		try {
			SBMLDocument document = reader.readSBML(fileName);
			if (document.getNumErrors() > 0) {
				throw new CoelRuntimeException("Errors found in SBML model " + fileName);
			}
			return document;
		} catch (XMLStreamException e) {
			throw new CoelRuntimeException("Errors found in SBML model " + fileName);
		} catch (IOException e) {
			throw new CoelRuntimeException("Errors found in SBML model " + fileName);
		}
	}

	private SBMLDocument getSBMLDocumentFromString(String content) {
		SBMLReader reader = new SBMLReader();
		try {
			SBMLDocument document = reader.readSBMLFromString(content);

			if (document.getNumErrors() > 0) {
				throw new CoelRuntimeException("Errors found in SBML model " + content);
			}
			return document;
		} catch (XMLStreamException e) {
			throw new CoelRuntimeException("Errors found in SBML model " + content);
		}
	}

//	public String convertSBMLFileToString(String fileName) {
//		SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
//		return sbmlDocument.toSBML();
//	}

	public ArtificialChemistry stringToArtificialChemistry(String sbmlString) {
		SBMLDocument sbmlDocument = getSBMLDocumentFromString(sbmlString);
		return toArtificialChemistry(sbmlDocument);
	}

	public ArtificialChemistry fileToArtificialChemistry(String fileName) {
		SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
		return toArtificialChemistry(sbmlDocument);
	}

	private ArtificialChemistry toArtificialChemistry(SBMLDocument sbmlDocument) {
		Model sbmlModel = sbmlDocument.getModel();

		ListOf<Compartment> compartments = sbmlModel.getListOfCompartments();
		ListOf<Species> species = sbmlModel.getListOfSpecies();
		ListOf<Reaction> reactions = sbmlModel.getListOfReactions();
		ListOf<Parameter> parameters = sbmlModel.getListOfParameters();
		ListOf<Rule> rules = sbmlModel.getListOfRules();
		int speciesNum = species.size();

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
			reactionSet.setLabel(compartment.getId());
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

			Set<AcSpeciesReactionAssociation> reactants = getSpeciesAssociations(reaction.getListOfReactants(), labelSpeciesStructureMap);
			Set<AcSpeciesReactionAssociation> products = getSpeciesAssociations(reaction.getListOfProducts(), labelSpeciesStructureMap);
            Set<AcSpeciesReactionAssociation> modifiers = getCatalystsAssociations(reaction.getListOfModifiers(), labelSpeciesStructureMap);

			acReaction.addSpeciesAssociations(reactants, AcSpeciesAssociationType.Reactant);
			acReaction.addSpeciesAssociations(products, AcSpeciesAssociationType.Product);
			acReaction.addSpeciesAssociations(modifiers, AcSpeciesAssociationType.Catalyst);

			KineticLaw kinetricLaw = reaction.getKineticLaw();
			ASTNode astNode = kinetricLaw.getMath();
			setFunction(acReaction, astNode, labelMagnitudeMap);

			skinCompartment.getReactionSet().addReaction(acReaction);
		}

		for (int ruleId = 0; ruleId < rules.size(); ruleId++) {
			Rule rule = rules.get(ruleId);
            if (rule.isAssignment()) {
                String variable = ((AssignmentRule) rule).getVariable();
                AcParameter acParameter = labelParameterMap.get(variable);
                ASTNode astNode = rule.getMath();
                setFunction(acParameter, astNode, labelMagnitudeMap);
            }
		}
		return alChemistry;
	}

	private Double[] getStoichiometricVector(
		int speciesNum,
		ListOf<SpeciesReference> speciesReferences,
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
		ListOf<SpeciesReference> speciesReferences,
		Map<String, AcSpecies> labelSpeciesMap
	) {
		Set<AcSpeciesReactionAssociation> speciesAssociations = new HashSet<AcSpeciesReactionAssociation>();
		for (int speciesRefIndex = 0; speciesRefIndex < speciesReferences.size(); speciesRefIndex++) {
			SimpleSpeciesReference speciesReference = speciesReferences.get(speciesRefIndex);
			AcSpecies acSpecies = labelSpeciesMap.get(speciesReference.getSpecies());
			double stoichiometry = 1.0;
			if (speciesReference instanceof SpeciesReference) {
				stoichiometry = ((SpeciesReference) speciesReference).getStoichiometry();
			}
			speciesAssociations.add(new AcSpeciesReactionAssociation(acSpecies, stoichiometry));
		}
		return speciesAssociations;
	}

	private Set<AcSpeciesReactionAssociation> getCatalystsAssociations(
		ListOf<ModifierSpeciesReference> speciesReferences,
		Map<String, AcSpecies> labelSpeciesMap
	) {
		Set<AcSpeciesReactionAssociation> speciesAssociations = new HashSet<AcSpeciesReactionAssociation>();
		for (int speciesRefIndex = 0; speciesRefIndex < speciesReferences.size(); speciesRefIndex++) {
			ModifierSpeciesReference speciesReference = speciesReferences.get(speciesRefIndex);
			AcSpecies acSpecies = labelSpeciesMap.get(speciesReference.getSpecies());
			speciesAssociations.add(new AcSpeciesReactionAssociation(acSpecies, (Double) null));
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
		String formula = astNode.toFormula();

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
		ArtificialChemistry alChemistry = sbmlConverter.fileToArtificialChemistry("perceptron_v06.xml");
		System.out.println(alChemistry);
	}
}