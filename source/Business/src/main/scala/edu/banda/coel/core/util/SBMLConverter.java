package edu.banda.coel.core.util;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Date;

import com.banda.chemistry.business.ArtificialChemistryUtil;
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

	final static String[] VAR_PRECEDING_STRING = new String[] {" ", ",", "\\(","\\*","-","/","\\^"};
	final static String[] VAR_NEXT_STRING = new String[] {" ", ",", "\\)","\\*","-","/","\\^"};
	final static String[] FUN_OPER_PART = new String[] {"(", ")", "+","*","-","/","^"};
	private final FunctionUtility functionUtility = new FunctionUtility();
    private final ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

    private final boolean ignoreErrorsQuietly;

	public SBMLConverter(boolean ignoreErrorsQuietly) {
        this.ignoreErrorsQuietly = ignoreErrorsQuietly;
	}

	private SBMLDocument getSBMLDocumentFromFile(String fileName) {
		SBMLReader reader = new SBMLReader();
		try {
			SBMLDocument document = reader.readSBML(fileName);
			if (document.getNumErrors() > 0) {
                throw new CoelRuntimeException("Errors found in SBML model: " + errorToString(document.getListOfErrors()));
			}
			return document;
		} catch (XMLStreamException e) {
			throw new CoelRuntimeException("SBML model is not a valid XML.");
		} catch (IOException e) {
			throw new CoelRuntimeException("Error loading an SBML file " + fileName);
		}
	}

	private SBMLDocument getSBMLDocumentFromString(String content) {
		SBMLReader reader = new SBMLReader();
		try {
			SBMLDocument document = reader.readSBMLFromString(content);

			if (document.getNumErrors() > 0) {
				throw new CoelRuntimeException("Errors found in SBML model: " + errorToString(document.getListOfErrors()));
			}
			return document;
		} catch (XMLStreamException e) {
			throw new CoelRuntimeException("SBML model is not a valid XML.");
		}
	}

    private String errorToString(SBMLErrorLog errorLog) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < errorLog.getErrorCount(); i++) {
           sb.append(errorLog.getError(i).getMessage());
        }
        return sb.toString();
    }

//	public String convertSBMLFileToString(String fileName) {
//		SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
//		return sbmlDocument.toSBML();
//	}

    public AcCompartment stringToCompartment(String sbmlString, String compartmentLabel) {
        SBMLDocument sbmlDocument = getSBMLDocumentFromString(sbmlString);
        return toCompartment(sbmlDocument.getModel(), compartmentLabel);
    }

    public AcCompartment fileToCompartment(String fileName, String compartmentLabel) {
        SBMLDocument sbmlDocument = getSBMLDocumentFromFile(fileName);
        return toCompartment(sbmlDocument.getModel(), compartmentLabel);
    }

//	public SBMLDocument compartmentToSBML(AcCompartment compartment) {
//        SBMLDocument smbl = new SBMLDocument();
//		return null;
//	}

    private AcCompartment toCompartment(Model sbmlModel, String compartmentLabel) {
        ListOf<Compartment> compartments = sbmlModel.getListOfCompartments();
        ListOf<Species> species = sbmlModel.getListOfSpecies();
        ListOf<Reaction> reactions = sbmlModel.getListOfReactions();
        ListOf<Parameter> parameters = sbmlModel.getListOfParameters();
        ListOf<Rule> rules = sbmlModel.getListOfRules();
        int speciesNum = species.size();

        Map<String, AcCompartment> labelCompartmentMap = new HashMap<String, AcCompartment>();
        AcCompartment skinCompartment = null;
        for (int compartmentId = 0; compartmentId < compartments.size(); compartmentId++) {
            Compartment compartment = compartments.get(compartmentId);

            AcCompartment acCompartment = new AcCompartment();
            acCompartment.setLabel(compartment.getId());
            AcReactionSet reactionSet = new AcReactionSet();

            AcSpeciesSet speciesSet = new AcSpeciesSet();
            speciesSet.initVarSequenceNum();

            AcParameterSet parameterSet = new AcParameterSet();
            parameterSet.setCreateTime(speciesSet.getCreateTime());

            speciesSet.setParameterSet(parameterSet);
            parameterSet.setSpeciesSet(speciesSet);

            reactionSet.setSpeciesSet(speciesSet);
            reactionSet.setLabel(compartment.getId());
            acCompartment.setReactionSet(reactionSet);

            labelCompartmentMap.put(acCompartment.getLabel(), acCompartment);
			if (compartmentId == 0) {
				// TODO - support hierarchical compartments
				skinCompartment = acCompartment;
			}
        }

        AcReactionSet skinReactionSet = skinCompartment.getReactionSet();
        AcSpeciesSet skinSpeciesSet = skinReactionSet.getSpeciesSet();
        AcParameterSet skinParameterSet = skinSpeciesSet.getParameterSet();
        skinCompartment.setLabel(compartmentLabel);
        skinReactionSet.setLabel(compartmentLabel);
        skinSpeciesSet.setName(compartmentLabel);
        skinParameterSet.setName(compartmentLabel);

        Map<String, AcSpecies> labelSpeciesMap = new HashMap<String, AcSpecies>();
        for (int speciesIndex = 0; speciesIndex < speciesNum; speciesIndex++) {
            Species oneSpecies = species.get(speciesIndex);

            AcSpecies acSpecies = new AcSpecies();
            acSpecies.setLabel(oneSpecies.getId());

//            AcCompartment associatedCompartment = labelCompartmentMap.get(oneSpecies.getCompartment());
//            associatedCompartment.getReactionSet().getSpeciesSet().addVariable(acSpecies);
            skinSpeciesSet.addVariable(acSpecies);
            labelSpeciesMap.put(acSpecies.getLabel(), acSpecies);
        }

        Map<String, AcParameter> labelParameterMap = new HashMap<String, AcParameter>();
        for (int parameterIndex = 0; parameterIndex < parameters.size(); parameterIndex++) {
            Parameter parameter = parameters.get(parameterIndex);
            AcParameter acParameter = new AcParameter();
            acParameter.setLabel(parameter.getId());
            acParameter.setEvolFunction(Expression.Double(String.valueOf(parameter.getValue())));

            skinParameterSet.addVariable(acParameter);
            labelParameterMap.put(acParameter.getLabel(), acParameter);
        }

        Map<String, AcVariable> labelMagnitudeMap = new HashMap<String, AcVariable>();
        labelMagnitudeMap.putAll(labelSpeciesMap);
        labelMagnitudeMap.putAll(labelParameterMap);

        for (int reactionIndex = 0; reactionIndex < reactions.size(); reactionIndex++) {
            Reaction reaction = reactions.get(reactionIndex);
            AcReaction acReaction = new AcReaction();
            acReaction.setIndex(reactionIndex); // not needed?
            acReaction.setLabel(reaction.getId());
            acReaction.setSortOrder(reactionIndex);

            Set<AcSpeciesReactionAssociation> reactants = getSpeciesAssociations(reaction.getListOfReactants(), labelSpeciesMap);
            Set<AcSpeciesReactionAssociation> products = getSpeciesAssociations(reaction.getListOfProducts(), labelSpeciesMap);
            Set<AcSpeciesReactionAssociation> modifiers = getCatalystsAssociations(reaction.getListOfModifiers(), labelSpeciesMap);

            acReaction.addSpeciesAssociations(reactants, AcSpeciesAssociationType.Reactant);
            acReaction.addSpeciesAssociations(products, AcSpeciesAssociationType.Product);
            acReaction.addSpeciesAssociations(modifiers, AcSpeciesAssociationType.Catalyst);

            KineticLaw kinetricLaw = reaction.getKineticLaw();
            ASTNode astNode = kinetricLaw.getMath();
            setFunction(acReaction, astNode, skinSpeciesSet);

            skinReactionSet.addReaction(acReaction);
        }

        Set<AcSpecies> reactionRefSpecies = acUtil.getReferencedSpecies(skinReactionSet);

        for (int ruleId = 0; ruleId < rules.size(); ruleId++) {
            Rule rule = rules.get(ruleId);
            ASTNode astNode = rule.getMath();
            if (rule.isAssignment()) {
                String variableLabel = ((AssignmentRule) rule).getVariable();
                AcParameter acParameter = labelParameterMap.get(variableLabel);

				if (acParameter == null) {

                    AcSpecies acSpecies = labelSpeciesMap.get(variableLabel);
                    if (acSpecies == null) {
                        if (!ignoreErrorsQuietly)
                            throw new CoelRuntimeException("Undefined parameter '" + variableLabel + "' is used in an assignment rule.");
                    } else {
                        if (ignoreErrorsQuietly) {
                            if (!reactionRefSpecies.contains(acSpecies)) {
                                // species is not used in any reaction, let's rebrand it as a parameter
                                skinSpeciesSet.removeVariable(acSpecies);

                                acParameter = new AcParameter();
                                acParameter.setVariableIndex(acSpecies.getVariableIndex());
                                acParameter.setLabel(variableLabel);
                                skinParameterSet.addVariable(acParameter);

                                labelSpeciesMap.remove(variableLabel);
                                labelParameterMap.put(variableLabel, acParameter);

                                setFunction(acParameter, astNode, skinSpeciesSet);
                            }
                        } else
                            throw new CoelRuntimeException("Parameter '" + variableLabel + "' is used in an assignment rule but is defined as species.");
                    }

				} else {
                    setFunction(acParameter, astNode, skinSpeciesSet);
                }
            }
        }

        return skinCompartment;
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
            if (acSpecies == null) {
                if (!ignoreErrorsQuietly)
                    throw new CoelRuntimeException("Undefined species '" + speciesReference.getSpecies() + "' used in a reaction.");
            } else {
                double stoichiometry = 1.0;
                if (speciesReference instanceof SpeciesReference) {
                    stoichiometry = ((SpeciesReference) speciesReference).getStoichiometry();
                }
                speciesAssociations.add(new AcSpeciesReactionAssociation(acSpecies, stoichiometry));
            }
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
		AcSpeciesSet speciesSet
	) {
		functionHolder.setFunction(getExpression(astNode, speciesSet));
	}

	private Function<Double, Double> getExpression(
		ASTNode astNode,
		AcSpeciesSet speciesSet
	) {
		String formula = astNode.toFormula();

        formula = acUtil.replaceSpeciesAndParamaterLabelsWithIndexPlaceholders(formula, speciesSet);
		formula = replaceBinaryFunctionByOperator("pow", "^", formula);
		formula = replacePiecewise(formula);

		return Expression.Double(formula);
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
				final String[] parts = token.split(",");
                for (int i = 0; i < parts.length - 1; i += 2) {
                    String value = parts[i].trim();

                    String condition = parts[i + 1].trim();
                    condition = replaceBinaryFunctionByOperator("lt", " < ", condition);
                    condition = replaceBinaryFunctionByOperator("gt", " > ", condition);
                    condition = replaceBinaryFunctionByOperator("eq", " = ", condition);

                    sb.append("if(");
                    sb.append(condition);
                    sb.append(",");
                    sb.append(value);
                    sb.append(",");
                }

                sb.append(parts[parts.length - 1]);
                for (int i = 0; i < (parts.length / 2) - 1; i++) {
                    sb.append(")");
                }
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
		SBMLConverter sbmlConverter = new SBMLConverter(false);
		AcCompartment compartment = sbmlConverter.fileToCompartment("perceptron_v06.xml", "Perceptron");
//        AcCompartment compartment = sbmlConverter.fileToCompartment("BIOMD0000000399_SBML-L3V1.xml", "Perceptron");
		System.out.println(compartment);
	}
}