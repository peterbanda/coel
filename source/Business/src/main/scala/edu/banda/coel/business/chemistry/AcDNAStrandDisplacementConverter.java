package edu.banda.coel.business.chemistry;

import java.util.*;

import com.banda.chemistry.BndChemistryException;
import com.banda.chemistry.business.AcReactionSetConverter;
import com.banda.chemistry.domain.AcVariable.AcVariableLabelComparator;
import com.banda.chemistry.domain.*;

public class AcDNAStrandDisplacementConverter extends AcReactionSetConverter {

    public static class AcDNAStrandConversionStats {
        double cMax;
        double qMax;
        double sigmaMax;
        double gammaInverse;
    }

	private final double CMax;
	private final boolean includeWaste;

	private Map<AcSpecies, Double> speciesSigmaMap = new HashMap<AcSpecies, Double>();
	private Map<AcSpecies, Integer> speciesDomainMap = new HashMap<AcSpecies, Integer>();
	private Set<AcSpecies> speciesBufferChecked = new HashSet<AcSpecies>();

	private int maxDomainId = 0;

	public AcDNAStrandDisplacementConverter(AcReactionSet inputReactionSet, double CMax, boolean includeWaste) {
		super(inputReactionSet);
		this.CMax = CMax;
		this.includeWaste = includeWaste;
		speciesSigmaMap = calcSigmas();
		speciesDomainMap = associateDNAStructure();
		for (int speciesId : speciesDomainMap.values())
			maxDomainId = Math.max(4 * speciesId + 4, maxDomainId);
	}

	@Override
	protected void validate(AcReaction inputReaction) {
		if (inputReaction.hasSpeciesAssociations(AcSpeciesAssociationType.Catalyst)) {
			throw new BndChemistryException(getClass().getSimpleName() + " cannot convert a catalytic reaction '" + inputReaction.getLabel() + "'. Pure mass-action driven reactions expected.");
		}
		if (inputReaction.hasSpeciesAssociations(AcSpeciesAssociationType.Inhibitor)) {
			throw new BndChemistryException(getClass().getSimpleName() + " cannot convert an inhibitory reaction '" + inputReaction.getLabel() + "'. Pure mass-action driven reactions expected.");
		}
		if (inputReaction.getSpeciesAssociationsNum(AcSpeciesAssociationType.Reactant) > 2) {
			throw new BndChemistryException(getClass().getSimpleName() + " cannot convert a reaction with more than two reactants '" + inputReaction.getLabel() + "'.");
		}
        int reactantStoichiometry = sumReactantStoichiometry(inputReaction);
        if (reactantStoichiometry != 1 && reactantStoichiometry != 2) {
            throw new BndChemistryException(getClass().getSimpleName() + " cannot convert a reaction '" + inputReaction.getLabel() + "'. The number of reactants is expected to be 1 or 2.");
        }
	}

	@Override
	protected boolean isConvertible(AcReaction inputReaction) {
		// convert all reactions
		return true;
	}

    public AcDNAStrandConversionStats getStats() {
        AcDNAStrandConversionStats stats = new AcDNAStrandConversionStats();
        stats.cMax = CMax;
        stats.qMax = getQMax();
        stats.sigmaMax = getMaxSigma();
        stats.gammaInverse = getBufferingScalingFactor();
        return stats;
    }

	@Override
	protected Collection<AcReaction> convert(AcReaction inputReaction) {
		Collection<AcReaction> newReactions;
		int reactantStoichiometry = sumReactantStoichiometry(inputReaction);
		if (reactantStoichiometry == 1) {
			newReactions = convertUnimolecularReaction(inputReaction);			
		} else if (reactantStoichiometry == 2) {
			newReactions = convertBimolecularReaction(inputReaction);
		} else {
			throw new BndChemistryException("The number of reactants is expected to be 1 or 2.");	
		}

		// buffering
		final double maxSigma = getMaxSigma();
		for (AcSpeciesReactionAssociation reactantAssoc : inputReaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant)) {
			final AcSpecies reactant = reactantAssoc.getSpecies();
			if (!speciesBufferChecked.contains(reactant)) {
				final double sigma = speciesSigmaMap.get(reactant);
				if (sigma < maxSigma) {
					newReactions.add(createBufferingReaction(reactant));
				}
				speciesBufferChecked.add(reactant);
			}
		}
		return newReactions;
	}

	private Collection<AcReaction> convertUnimolecularReaction(AcReaction reaction) {
		final AcSpeciesReactionAssociation originalReactantAssoc = reaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant).get(0);
		final AcSpecies originalReactant = originalReactantAssoc.getSpecies();
		final Collection<AcSpeciesReactionAssociation> originalProductAssocs = reaction.getSpeciesAssociations(AcSpeciesAssociationType.Product);

		final Integer reactantDomainId = speciesDomainMap.get(originalReactant);
		int x1 = 4 * reactantDomainId + 1;
		int x2 = 4 * reactantDomainId + 2;
		int x3 = 4 * reactantDomainId + 3;
		int x4 = 4 * reactantDomainId + 4;

		List<Integer> productDomainIds = new ArrayList<Integer>();
		for (AcSpeciesReactionAssociation originalProductAssoc : originalProductAssocs) {
			final AcSpecies product = originalProductAssoc.getSpecies();
			int intStoichiometry = originalProductAssoc.getStoichiometricFactor().intValue();
			for (int i = 0; i < intStoichiometry; i++)
				productDomainIds.add(speciesDomainMap.get(product));
		}

		// G
		AcSpecies tempInDoubleStrand1 = new AcSpecies();
		tempInDoubleStrand1.setLabel("G_" + reaction.getLabel());
		StringBuilder gStructure = new StringBuilder();
		gStructure.append("{" + x1 + "^*}[" + x2 + " " + x3 + "^]");
		boolean firstProductChecked = false;
		if (!productDomainIds.isEmpty()) {
			gStructure.append("<");
			for (Integer productDomainId : productDomainIds) {
				if (firstProductChecked) gStructure.append(" "); 
				gStructure.append(4 * productDomainId + 4);
				gStructure.append(" ");
				gStructure.append(4 * productDomainId + 1);
				gStructure.append("^");
				firstProductChecked = true;
			}
			gStructure.append(">");
		}
		tempInDoubleStrand1.setStructure(gStructure.toString());

		// W1
		AcSpecies wasteDoubleStrand1 = new AcSpecies();
		wasteDoubleStrand1.setLabel("W1_" + reaction.getLabel());
		StringBuilder w1Structure = new StringBuilder();
		w1Structure.append("<" + x4 + ">[" + x1 + "^ " + x2 + " " + x3 + "^]");
		wasteDoubleStrand1.setStructure(w1Structure.toString());

		// O
		AcSpecies tempOutInSingleStrand = new AcSpecies();
		tempOutInSingleStrand.setLabel("O_" + reaction.getLabel());
		StringBuilder oStructure = new StringBuilder();
		oStructure.append("<" + x2 + " " + x3 + "^");
		for (Integer productDomainId : productDomainIds) {
			oStructure.append(" ");
			oStructure.append(4 * productDomainId + 4);
			oStructure.append(" ");
			oStructure.append(4 * productDomainId + 1);
			oStructure.append("^");
		}
		oStructure.append(">");
		tempOutInSingleStrand.setStructure(oStructure.toString());

		// T
		AcSpecies tempInDoubleStrand2 = new AcSpecies();
		tempInDoubleStrand2.setLabel("T_" + reaction.getLabel());
		StringBuilder tStructure = new StringBuilder();
		tStructure.append("{" + x3 + "^*}");
		firstProductChecked = false;
		for (Integer productDomainId : productDomainIds) {
			if (firstProductChecked) tStructure.append(":"); 			
			tStructure.append("[");
			tStructure.append(4 * productDomainId + 4);
			tStructure.append(" ");
			tStructure.append(4 * productDomainId + 1);
			tStructure.append("^]<");
			tStructure.append(4 * productDomainId + 2);
			tStructure.append(" ");
			tStructure.append(4 * productDomainId + 3);
			tStructure.append("^>");
			firstProductChecked = true;
		}
		tempInDoubleStrand2.setStructure(tStructure.toString());

		// W2
		AcSpecies wasteDoubleStrand2 = new AcSpecies();
		wasteDoubleStrand2.setLabel("W2_" + reaction.getLabel());
		StringBuilder w2Structure = new StringBuilder();
		w2Structure.append("<" + x2 + ">");
		w2Structure.append("[" + x3 + "^");
		for (Integer productDomainId : productDomainIds) {
			w2Structure.append(" ");
			w2Structure.append(4 * productDomainId + 4);
			w2Structure.append(" ");
			w2Structure.append(4 * productDomainId + 1);
			w2Structure.append("^");
		}
		w2Structure.append("]");
		wasteDoubleStrand2.setStructure(w2Structure.toString());

		// Reactions
		Collection<AcReaction> newReactions = new ArrayList<AcReaction>();

		// X_1 + G_1 -> (W1) + O_1
		AcReaction reaction1 = new AcReaction();
		reaction1.setLabel(reaction.getLabel() + "_1");
		reaction1.setEnabled(true);
		reaction1.addAssociationForSpecies(originalReactant, AcSpeciesAssociationType.Reactant);
		reaction1.addAssociationForSpecies(tempInDoubleStrand1, AcSpeciesAssociationType.Reactant);
		if (includeWaste)
			reaction1.addAssociationForSpecies(wasteDoubleStrand1, AcSpeciesAssociationType.Product);
		reaction1.addAssociationForSpecies(tempOutInSingleStrand, AcSpeciesAssociationType.Product);

		double newRateConstant1 = getNewRateConstant(reaction);
		reaction1.setForwardRateConstants(new Double[] {newRateConstant1});
		newReactions.add(reaction1);

		if (reaction.hasSpeciesAssociations(AcSpeciesAssociationType.Product)) {
			// O_1 + T_1 -> (W2) + X_2 + ...
			
			AcReaction reaction2 = new AcReaction();
			reaction2.setLabel(reaction.getLabel() + "_2");
			reaction2.setEnabled(true);
			reaction2.addAssociationForSpecies(tempOutInSingleStrand, AcSpeciesAssociationType.Reactant);
			reaction2.addAssociationForSpecies(tempInDoubleStrand2, AcSpeciesAssociationType.Reactant);
			if (includeWaste)
				reaction2.addAssociationForSpecies(wasteDoubleStrand2, AcSpeciesAssociationType.Product);

			for (AcSpeciesReactionAssociation originalProductAssoc : originalProductAssocs) {
				reaction2.addSpeciesAssociation(
						new AcSpeciesReactionAssociation(originalProductAssoc.getSpecies(), originalProductAssoc.getType(), originalProductAssoc.getStoichiometricFactor()));				
			}
			reaction2.setForwardRateConstants(new Double[] {getQMax()});
			newReactions.add(reaction2);
		}
		return newReactions;
	}

	private Collection<AcReaction> convertBimolecularReaction(AcReaction reaction) {
		final List<AcSpeciesReactionAssociation> originalReactantAssocs = reaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant);
		final AcSpeciesReactionAssociation originalFirstReactantAssoc = originalReactantAssocs.get(0);
		AcSpeciesReactionAssociation originalSecondReactantAssoc = null;
		if (originalReactantAssocs.size() == 2) {
			originalSecondReactantAssoc = originalReactantAssocs.get(1);
		} else {
			originalSecondReactantAssoc = originalReactantAssocs.get(0);
		}
		final Collection<AcSpeciesReactionAssociation> originalProductAssocs = reaction.getSpeciesAssociations(AcSpeciesAssociationType.Product);

		final Integer reactant1DomainId = speciesDomainMap.get(originalFirstReactantAssoc.getSpecies());
		final Integer reactant2DomainId = speciesDomainMap.get(originalSecondReactantAssoc.getSpecies());

		List<Integer> productDomainIds = new ArrayList<Integer>();
		for (AcSpeciesReactionAssociation originalProductAssoc : originalProductAssocs) {
			final AcSpecies product = originalProductAssoc.getSpecies();
			int intStoichiometry = originalProductAssoc.getStoichiometricFactor().intValue();
			for (int i = 0; i < intStoichiometry; i++)
				productDomainIds.add(speciesDomainMap.get(product));
		}

		int x11 = 4 * reactant1DomainId + 1;
		int x12 = 4 * reactant1DomainId + 2;
		int x13 = 4 * reactant1DomainId + 3;
		int x14 = 4 * reactant1DomainId + 4;

		int x21 = 4 * reactant2DomainId + 1;
		int x22 = 4 * reactant2DomainId + 2;
		int x23 = 4 * reactant2DomainId + 3;	
		int x24 = 4 * reactant2DomainId + 4;

		// L
		AcSpecies tempInDoubleStrand1 = new AcSpecies();
		tempInDoubleStrand1.setLabel("L_" + reaction.getLabel());
		StringBuilder lStructure = new StringBuilder();
		lStructure.append("{" + x11 + "^*}[" + x12 + " " + x13 + "^]");
		lStructure.append("[" + x21 + "^ " + x22 + " " + x23 + "^]");
		boolean firstProductChecked = false;
		if (!productDomainIds.isEmpty()) {
			lStructure.append("<");
			for (Integer productDomainId : productDomainIds) {
				if (firstProductChecked) lStructure.append(" "); 
				lStructure.append(4 * productDomainId + 4);
				lStructure.append(" ");
				lStructure.append(4 * productDomainId + 1);
				lStructure.append("^");
				firstProductChecked = true;
			}
			lStructure.append(">");
		}
		tempInDoubleStrand1.setStructure(lStructure.toString());		

		// B
		AcSpecies tempOutSingleStrand1 = new AcSpecies();
		tempOutSingleStrand1.setLabel("B_" + reaction.getLabel());
		StringBuilder bStructure = new StringBuilder();
		bStructure.append("<" + x12 + " " + x13 + "^ " + x21 + "^>");
		tempOutSingleStrand1.setStructure(bStructure.toString());	

		// H
		AcSpecies tempOutInDoubleStrand1 = new AcSpecies();
		tempOutInDoubleStrand1.setLabel("H_" + reaction.getLabel());
		StringBuilder hStructure = new StringBuilder();
		hStructure.append("<" + x14 + ">[" + x11 + "^ " + x12 + " " + x13 + "^]:");
		hStructure.append("{" + x21+ "^*}[" + x22 + " " + x23 + "^]");
		if (!productDomainIds.isEmpty()) {
			hStructure.append("<");
			firstProductChecked = false;
			for (Integer productDomainId : productDomainIds) {
				if (firstProductChecked) hStructure.append(" "); 
				hStructure.append(4 * productDomainId + 4);
				hStructure.append(" ");
				hStructure.append(4 * productDomainId + 1);
				hStructure.append("^");
				firstProductChecked = true;
			}
			hStructure.append(">");
		}
		tempOutInDoubleStrand1.setStructure(hStructure.toString());

		// W1 
		AcSpecies wasteDoubleStrand1 = new AcSpecies();
		wasteDoubleStrand1.setLabel("W1_" + reaction.getLabel());
		StringBuilder w1Structure = new StringBuilder();
		w1Structure.append("<" + x14 + ">[" + x11 + "^ " + x12 + " " + x13 + "^]:");
		w1Structure.append("<" + x24 + ">[" + x21 + "^ " + x22 + " " + x23 + "^]");
		wasteDoubleStrand1.setStructure(w1Structure.toString());

		// O
		AcSpecies tempOutInSingleStrand2 = new AcSpecies();
		tempOutInSingleStrand2.setLabel("O_" + reaction.getLabel());
		StringBuilder oStructure = new StringBuilder();
		oStructure.append("<" + x22 + " " + x23 + "^");
		for (Integer productDomainId : productDomainIds) {
			oStructure.append(" ");
			oStructure.append(4 * productDomainId + 4);
			oStructure.append(" ");
			oStructure.append(4 * productDomainId + 1);
			oStructure.append("^");
		}
		oStructure.append(">");
		tempOutInSingleStrand2.setStructure(oStructure.toString());	

		// T
		AcSpecies tempInDoubleStrand3 = new AcSpecies();
		tempInDoubleStrand3.setLabel("T_" + reaction.getLabel());
		StringBuilder tStructure = new StringBuilder();
		tStructure.append("{" + x23 + "^*}");
		firstProductChecked = false;
		for (Integer productDomainId : productDomainIds) {
			if (firstProductChecked) tStructure.append(":");
			tStructure.append("[");
			tStructure.append(4 * productDomainId + 4);
			tStructure.append(" ");
			tStructure.append(4 * productDomainId + 1);
			tStructure.append("^");
			tStructure.append("]");
			tStructure.append("<");
			tStructure.append(4 * productDomainId + 2);
			tStructure.append(" ");
			tStructure.append(4 * productDomainId + 3);
			tStructure.append("^");
			tStructure.append(">");
			firstProductChecked = true;
		}
		tempInDoubleStrand3.setStructure(tStructure.toString());

		// W2
		AcSpecies wasteDoubleStrand2 = new AcSpecies();
		wasteDoubleStrand2.setLabel("W2_" + reaction.getLabel());
		StringBuilder w2Structure = new StringBuilder();
		w2Structure.append("<" + x22 + ">");
		w2Structure.append("[" + x23 + "^");
		for (Integer productDomainId : productDomainIds) {
			w2Structure.append(" ");
			w2Structure.append(4 * productDomainId + 4);
			w2Structure.append(" ");
			w2Structure.append(4 * productDomainId + 1);
			w2Structure.append("^");
		}
		w2Structure.append("]");
		wasteDoubleStrand2.setStructure(w2Structure.toString());

		
		// Reactions

		Collection<AcReaction> newReactions = new ArrayList<AcReaction>();
		
		// X_1 + L_1 -> H_1 + B_1
		AcReaction reaction1 = new AcReaction();
		reaction1.setLabel(reaction.getLabel() + "_1");
		reaction1.setEnabled(true);
		reaction1.addAssociationForSpecies(originalFirstReactantAssoc.getSpecies(), AcSpeciesAssociationType.Reactant);
		reaction1.addAssociationForSpecies(tempInDoubleStrand1, AcSpeciesAssociationType.Reactant);
		reaction1.addAssociationForSpecies(tempOutInDoubleStrand1, AcSpeciesAssociationType.Product);
		reaction1.addAssociationForSpecies(tempOutSingleStrand1, AcSpeciesAssociationType.Product);

		double newRateConstant1 = getNewRateConstant(reaction);
		reaction1.setForwardRateConstants(new Double[] {newRateConstant1});
		reaction1.setReverseRateConstants(new Double[] {getQMax()});
		newReactions.add(reaction1);

		// X_2 + H_1 -> (W_1) + O_1
		AcReaction reaction2 = new AcReaction();
		reaction2.setLabel(reaction.getLabel() + "_2");
		reaction2.setEnabled(true);
		reaction2.addAssociationForSpecies(originalSecondReactantAssoc.getSpecies(), AcSpeciesAssociationType.Reactant);
		reaction2.addAssociationForSpecies(tempOutInDoubleStrand1, AcSpeciesAssociationType.Reactant);
		if (includeWaste)
			reaction2.addAssociationForSpecies(wasteDoubleStrand1, AcSpeciesAssociationType.Product);
		reaction2.addAssociationForSpecies(tempOutInSingleStrand2, AcSpeciesAssociationType.Product);

		reaction2.setForwardRateConstants(new Double[] {getQMax()});
		newReactions.add(reaction2);

		// O_1 + T_1 -> (W_2) + X_2 + ...
		if (reaction.hasSpeciesAssociations(AcSpeciesAssociationType.Product)) {
			AcReaction reaction3 = new AcReaction();
			reaction3.setLabel(reaction.getLabel() + "_3");
			reaction3.setEnabled(true);
			reaction3.addAssociationForSpecies(tempOutInSingleStrand2, AcSpeciesAssociationType.Reactant);
			reaction3.addAssociationForSpecies(tempInDoubleStrand3, AcSpeciesAssociationType.Reactant);

			if (includeWaste)
				reaction3.addAssociationForSpecies(wasteDoubleStrand2, AcSpeciesAssociationType.Product);

			for (AcSpeciesReactionAssociation originalProductAssoc : originalProductAssocs) {
				reaction3.addSpeciesAssociation(
						new AcSpeciesReactionAssociation(originalProductAssoc.getSpecies(), originalProductAssoc.getType(), originalProductAssoc.getStoichiometricFactor()));				
			}

			reaction3.setForwardRateConstants(new Double[] {getQMax()});
			newReactions.add(reaction3);
		}
		return newReactions;
	}

	private AcReaction createBufferingReaction(AcSpecies species) {
		final String speciesLabel = species.getLabel();

		// Buffering: X_1 + L_S1 = HS_1 + BS_1

		final Integer speciesDomainId = speciesDomainMap.get(species);
		int x1 = 4 * speciesDomainId + 1;
		int x2 = 4 * speciesDomainId + 2;
		int x3 = 4 * speciesDomainId + 3;
		int x4 = 4 * speciesDomainId + 4;
		maxDomainId++;

		// LS
		AcSpecies tempInDoubleStrandBuffering = new AcSpecies();
		tempInDoubleStrandBuffering.setLabel("LS_" + speciesLabel);
		StringBuilder lsStructure = new StringBuilder();
		lsStructure.append("{" + x1 + "^*}[" + x2 + " " + x3 + "^ " + maxDomainId + "^]");
		tempInDoubleStrandBuffering.setStructure(lsStructure.toString());

		// HS
		AcSpecies tempOutDoubleStrandBuffering = new AcSpecies();
		tempOutDoubleStrandBuffering.setLabel("HS_" + speciesLabel);
		StringBuilder hsStructure = new StringBuilder();
		hsStructure.append("<" + x4 + ">[" + x1 + "^ " + x2 + " " + x3 + "^]{" + maxDomainId + "^*}");
		tempOutDoubleStrandBuffering.setStructure(hsStructure.toString());

		// BS
		AcSpecies tempOutSingleStrandBuffering = new AcSpecies();
		tempOutSingleStrandBuffering.setLabel("BS_" + speciesLabel);
		StringBuilder bsStructure = new StringBuilder();
		bsStructure.append("<" + x2 + " " + x3 + "^ " + maxDomainId + "^>");
		tempOutSingleStrandBuffering.setStructure(bsStructure.toString());

		AcReaction reaction = new AcReaction();
		reaction.setLabel(speciesLabel + "_Buf");
		reaction.setEnabled(true);

		reaction.addAssociationForSpecies(species, AcSpeciesAssociationType.Reactant);
		reaction.addAssociationForSpecies(tempInDoubleStrandBuffering, AcSpeciesAssociationType.Reactant);
		reaction.addAssociationForSpecies(tempOutDoubleStrandBuffering, AcSpeciesAssociationType.Product);
		reaction.addAssociationForSpecies(tempOutSingleStrandBuffering, AcSpeciesAssociationType.Product);

		final double newRateConstant = getNewBufferingRateConstant(species);
		reaction.setForwardRateConstants(new Double[] {newRateConstant});
		reaction.setReverseRateConstants(new Double[] {getQMax()});

		return reaction;
	}

	private double getNewRateConstant(AcReaction reaction) {
		final int reactantNum = sumReactantStoichiometry(reaction);
		double originalRateConstant = reaction.getForwardRateConstants()[0];
		if (reactantNum == 1) {
			return originalRateConstant * getBufferingScalingFactor() / CMax;			
		} else if (reactantNum == 2) {
			return originalRateConstant * getBufferingScalingFactor();
		}
		throw new BndChemistryException("The number of reactants is expected to be 1 or 2.");
	}

	private double getNewBufferingRateConstant(AcSpecies species) {
		final double sigma = speciesSigmaMap.get(species);
		return getBufferingScalingFactor() * (getMaxSigma() - sigma);
	}

	private Map<AcSpecies, Double> calcSigmas() {
		Map<AcSpecies, Double> speciesSigmaMap = new HashMap<AcSpecies, Double>();
		Collection<AcSpecies> inputSpecies = acUtil.getReferencedSpecies(getInputReactionSet());
		for (final AcSpecies species : inputSpecies) {
			speciesSigmaMap.put(species, 0d);
		}
		for (AcReaction inputReaction : getInputReactions()) {
			List<AcSpeciesReactionAssociation> reactanAssocs = inputReaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant);
			if (reactanAssocs.size() == 2 || (reactanAssocs.size() == 1 && reactanAssocs.get(0).getStoichiometricFactor() == 2d)) {
				final AcSpeciesReactionAssociation firstReactantAssoc = reactanAssocs.get(0);
				final AcSpecies firstReactant = firstReactantAssoc.getSpecies();

				double oldValue = speciesSigmaMap.get(firstReactant);
				Double rateConstant = inputReaction.getRateConstants(true)[0];
				speciesSigmaMap.put(firstReactant, rateConstant + oldValue);
			}
		}
		return speciesSigmaMap;
	}

	private Map<AcSpecies, Integer> associateDNAStructure() {
		Map<AcSpecies, Integer> speciesDomainMap = new HashMap<AcSpecies, Integer>();
		
		List<AcSpecies> sortedSpecies = new ArrayList<AcSpecies>(); 
		sortedSpecies.addAll(acUtil.getReferencedSpecies(getInputReactionSet()));
		Collections.sort(sortedSpecies, new AcVariableLabelComparator());

		int count = 0;
		for (final AcSpecies species : sortedSpecies) {
			speciesDomainMap.put(species, count);
			int x1 = 4 * count + 4;
			int x2 = 4 * count + 1;
			int x3 = 4 * count + 2;
			int x4 = 4 * count + 3;
			species.setStructure("<" + x1 + " " + x2 + "^ " + x3 + " " + x4 + "^>");
			count++;
		}
		return speciesDomainMap;
	}

	private double getMaxSigma() {
		double maxSigma = 0;
		for (Double sigma : speciesSigmaMap.values()) {
			maxSigma = Math.max(maxSigma, sigma);
		}
		return maxSigma;
	}

	private double getQMax() {
		final double sigmaMax = getMaxSigma();

		final double QMax1 = getMaxRateConstantForUnimolecular()/CMax + sigmaMax;
		final double QMax2 = getMaxRateConstantForBimolecular() + sigmaMax;
		double QMax3 = 0;
		for (double sigma : speciesSigmaMap.values()) {
			QMax3 = Math.max(QMax3, 2 * sigmaMax - sigma);
		}
		return Math.max(QMax1, Math.max(QMax2, QMax3));
	}

	// gamma ^-1
	private double getBufferingScalingFactor() {
		final double QMax = getQMax();
		return QMax / (QMax - getMaxSigma());
	}

	private Collection<AcReaction> getReactions(int numberOfReactants) {
		Collection<AcReaction> selectedReactions = new ArrayList<AcReaction>();
		for (final AcReaction inputReaction : getInputReactions()) {
			int totalStoichiometry = sumReactantStoichiometry(inputReaction);
			if (totalStoichiometry == numberOfReactants) {
				selectedReactions.add(inputReaction);
			}
		}
		return selectedReactions;
	}

	private int sumReactantStoichiometry(AcReaction reaction) {
		int totalStoichiometry = 0;
		for (AcSpeciesReactionAssociation reactantAssoc : reaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant)) {
			if (reactantAssoc.getStoichiometricFactor() != null) {
				totalStoichiometry += reactantAssoc.getStoichiometricFactor().intValue();
			} else {
				totalStoichiometry++;
			}
		}
		return totalStoichiometry;
	}

	private double getMaxRateConstantForUnimolecular() {
		return getMaxRateConstant(getReactions(1));
	}

	private double getMaxRateConstantForBimolecular() {
		return getMaxRateConstant(getReactions(2));
	}

	private double getMaxRateConstant(Collection<AcReaction> reactions) {
		double maxRateConstant = 0;
		for (final AcReaction inputReaction : reactions) {
			final double rateConstant = inputReaction.getRateConstants(true)[0];
			maxRateConstant = Math.max(maxRateConstant, rateConstant);
		}
		return maxRateConstant;
	}
}