package edu.banda.coel.server.scripts;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.AcReplicator;
import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcParameterSet;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionGroup;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesAssociationType;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class LP100 extends CoelTest {

	private static final long SPECIES_SET_ID = 3475l;
	private static final String SPECIES_SET_NAME = "AASP n = 100";
	private static final long REACTION_SET_ID = 3693l;
	private static final long SIM_CONFIG_ID = 1012l;

	@Autowired
	GenericDAO<AcSpeciesSet, Long> acSpeciesSetDAO;

	@Autowired
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	@Autowired
	GenericDAO<AcCompartment, Long> acCompartmentDAO;

	@Autowired
	GenericDAO<ArtificialChemistry, Long> artificialChemistryDAO;

	@Autowired
	GenericDAO<AcSimulationConfig, Long> acSimulationConfigDAO;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();
	AcReplicator replicator = AcReplicator.getInstance();

//	@Test
//	@Rollback(false)
	public void createSpeciesSet() {
		AcSpeciesSet parentSpeciesSet = acSpeciesSetDAO.get(SPECIES_SET_ID);

		AcSpeciesSet newSpeciesSet = new AcSpeciesSet();
		newSpeciesSet.setCreatedBy(parentSpeciesSet.getCreatedBy());
		newSpeciesSet.setName(SPECIES_SET_NAME);

		AcParameterSet newParameterSet = new AcParameterSet();
		newParameterSet.setName(newSpeciesSet.getName());
		newParameterSet.setCreateTime(newSpeciesSet.getCreateTime());
		newParameterSet.setCreatedBy(newSpeciesSet.getCreatedBy());

		newSpeciesSet.setParameterSet(newParameterSet);
		newParameterSet.setSpeciesSet(newSpeciesSet);
		newSpeciesSet.setParentSpeciesSet(parentSpeciesSet);		
		newSpeciesSet.initVarSequenceNum();

		for (int n = 6; n <= 100; n++) {
			newSpeciesSet.addVariable(new AcSpecies("X"+n));
			newSpeciesSet.addVariable(new AcSpecies("X"+n+"Y"));
			newSpeciesSet.addVariable(new AcSpecies("W"+n));
			newSpeciesSet.addVariable(new AcSpecies("W"+n+"ne"));
		}

		acSpeciesSetDAO.save(newSpeciesSet);
		assertTrue(true);
	}

	@Test
	@Rollback(false)
	public void createReactionSets() {
		AcReactionSet reactionSetN5 = acReactionSetDAO.get(REACTION_SET_ID);
		AcSimulationConfig simConfig = acSimulationConfigDAO.get(SIM_CONFIG_ID);
		AcSpeciesSet speciesSetN100 = acSpeciesSetDAO.get(3526l);
		Map<String,AcSpecies> labelSpeciesMap = acUtil.getLabelVariableMap(speciesSetN100);

		for (int n = 6; n <= 100; n++) {
			final String reactionSetLabel = "AASPr v4.4.3 n = " + n;
			AcReactionSet reactionSetN = replicator.cloneReactionSetWithReactionsAndGroups(reactionSetN5);
			reactionSetN.setLabel(reactionSetLabel);
			replicator.nullIdAndVersionRecursively(reactionSetN);
			reactionSetN.setSpeciesSet(speciesSetN100);

			final List<AcReactionGroup> groups = reactionSetN.getGroups();
			final AcReactionGroup ioGroup = groups.get(0);
			final AcReactionGroup anihGroup = groups.get(1);
			final AcReactionGroup wAdaptGroup = groups.get(3);
			final AcSpecies output = labelSpeciesMap.get("Y");
			final AcSpecies outputNeg = labelSpeciesMap.get("Yne");
			final AcSpecies weightPo = labelSpeciesMap.get("Wpo");
			final AcSpecies weightNe = labelSpeciesMap.get("Wne");

			for (int i = 6; i <= n; i++) {
				int index = reactionSetN.getReactionsNum();
				final String ioNegLabel = "R" + (index + 1) + "-IO-X" + i + "-neg";
				final String ioPosLabel = "R" + (index + 2) + "-IO-X" + i + "-pos";
				final String adaptPosLabel = "R" + (index + 3) + "-Adapt-W" + i + "-pos";
				final String adaptNegLabel = "R" + (index + 4) + "-Adapt-W" + i + "-neg";
				final String adaptAnihLabel = "R" + (index + 5) + "-Adapt-W" + i + "-anih";

				final AcSpecies input = labelSpeciesMap.get("X" + i);
				final AcSpecies inputCached = labelSpeciesMap.get("X" + i + "Y");
				final AcSpecies weight = labelSpeciesMap.get("W" + i);
				final AcSpecies weightNeg = labelSpeciesMap.get("W" + i + "ne");

				addReaction(reactionSetN, ioGroup, ioNegLabel,
						new AcSpecies[]{input}, new AcSpecies[]{inputCached, outputNeg}, 1);

				addReaction(reactionSetN, ioGroup, ioPosLabel,
						new AcSpecies[]{weight, input}, new AcSpecies[]{weight, inputCached, output}, 1);

				addReaction(reactionSetN, wAdaptGroup, adaptPosLabel,
						new AcSpecies[]{weightPo, inputCached}, new AcSpecies[]{weight, inputCached}, 1);

				addReaction(reactionSetN, wAdaptGroup, adaptNegLabel,
						new AcSpecies[]{weightNe, inputCached}, new AcSpecies[]{weightNeg, inputCached}, 1);

				addReaction(reactionSetN, anihGroup, adaptAnihLabel,
						new AcSpecies[]{weight, weightNeg}, new AcSpecies[0], 5);
			}
			acReactionSetDAO.save(reactionSetN);
			
			AcCompartment compartmentN = new AcCompartment();
			compartmentN.setCreatedBy(reactionSetN.getCreatedBy());
			compartmentN.setLabel(reactionSetLabel);
			compartmentN.setReactionSet(reactionSetN);
			acCompartmentDAO.save(compartmentN);

			ArtificialChemistry artificialChemistryN = new ArtificialChemistry();
			artificialChemistryN.setCreatedBy(reactionSetN.getCreatedBy());
			artificialChemistryN.setName(reactionSetLabel + " (RK 0.05)");
			artificialChemistryN.setSkinCompartment(compartmentN);
			artificialChemistryN.setSimulationConfig(simConfig);
			artificialChemistryDAO.save(artificialChemistryN);
		}
		assertTrue(true);
	}

	private void addReaction(
		AcReactionSet reactionSet,
		AcReactionGroup reactionGroup,
		String label,
		AcSpecies[] reactants,
		AcSpecies[] products,
		double rateConstant
	) {
		AcReaction reaction = new AcReaction();
		reaction.setLabel(label);
		reaction.setForwardRateConstant(rateConstant);
		reaction.addAssociationsForSpecies(Arrays.asList(reactants), AcSpeciesAssociationType.Reactant);
		reaction.addAssociationsForSpecies(Arrays.asList(products), AcSpeciesAssociationType.Product);
		reactionSet.addReaction(reaction);
		reactionGroup.addReaction(reaction);
	}
}