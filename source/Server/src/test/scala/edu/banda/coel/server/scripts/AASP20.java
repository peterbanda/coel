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
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionGroup;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.chemistry.domain.AcSpecies;
import com.banda.chemistry.domain.AcSpeciesAssociationType;
import com.banda.chemistry.domain.AcSpeciesSet;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class AASP20 extends CoelTest {

	private static final long SPECIES_SET_ID = 3526l;
	private static final long REACTION_SET_ID = 3542l;

	@Autowired
	GenericDAO<AcSpeciesSet, Long> acSpeciesSetDAO;

	@Autowired
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	@Autowired
	GenericDAO<AcCompartment, Long> acCompartmentDAO;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();
	AcReplicator replicator = AcReplicator.getInstance();

	@Test
	@Rollback(false)
	public void createReactionSets() {
		AcReactionSet reactionSetN5 = acReactionSetDAO.get(REACTION_SET_ID);
		AcSpeciesSet speciesSetN100 = acSpeciesSetDAO.get(SPECIES_SET_ID);
		Map<String,AcSpecies> labelSpeciesMap = acUtil.getLabelVariableMap(speciesSetN100);

		for (int n = 6; n <= 20; n++) {
			final String reactionSetLabel = "AASPr v4.1 n = " + n;
			AcReactionSet reactionSetN = replicator.cloneReactionSetWithReactionsAndGroups(reactionSetN5);
			reactionSetN.setLabel(reactionSetLabel);
			replicator.nullIdAndVersionRecursively(reactionSetN);
			reactionSetN.setSpeciesSet(speciesSetN100);

			final List<AcReactionGroup> groups = reactionSetN.getGroups();

			final AcReactionGroup ioAnihGroup = groups.get(2);
			final AcReactionGroup ioGroup = groups.get(3);

			final AcReactionGroup wAdaptNegGroup = groups.get(8);
			final AcReactionGroup anihGroup = groups.get(9);
			final AcReactionGroup wAdaptPosGroup = groups.get(10);

			final AcSpecies output = labelSpeciesMap.get("Y");
			final AcSpecies weightPo = labelSpeciesMap.get("Wpo");
			final AcSpecies weightNe = labelSpeciesMap.get("Wne");

			for (int i = 6; i <= n; i++) {
				int index = reactionSetN.getReactionsNum();

				final String ioAnihLabel = "R" + (index + 1) + "-X" + i + "-Anih";
				final String ioPosLabel = "R" + (index + 2) + "-X" + i + "-Weight";
				final String adaptNegLabel = "R" + (index + 3) + "-Adapt-W" + i + "-neg";
				final String adaptAnihLabel = "R" + (index + 4) + "-W" + i + "-anih";
				final String adaptPosLabel = "R" + (index + 5) + "-Adapt-W" + i + "-pos";

				final AcSpecies input = labelSpeciesMap.get("X" + i);
				final AcSpecies inputCached = labelSpeciesMap.get("X" + i + "Y");
				final AcSpecies weight = labelSpeciesMap.get("W" + i);
				final AcSpecies weightNeg = labelSpeciesMap.get("W" + i + "ne");

				addReaction(reactionSetN, ioAnihGroup, ioAnihLabel,
						new AcSpecies[]{input, output},
						new AcSpecies[0],
						new AcSpecies[0],
						new Double[]{0.3904706426775567});

				addReaction(reactionSetN, ioGroup, ioPosLabel,
						new AcSpecies[]{input},
						new AcSpecies[]{inputCached, output},
						new AcSpecies[]{weight},
						new Double[]{0.4357928982276042, 0.12272659100585225});

				addReaction(reactionSetN, wAdaptNegGroup, adaptNegLabel,
						new AcSpecies[]{weightNe},
						new AcSpecies[]{weightNeg},
						new AcSpecies[]{inputCached},
						new Double[]{0.18889058367155298, 1.6787862056733152});

				addReaction(reactionSetN, anihGroup, adaptAnihLabel,
						new AcSpecies[]{weight, weightNeg},
						new AcSpecies[0],
						new AcSpecies[0],
						new Double[]{0.2416256073745371});

				addReaction(reactionSetN, wAdaptPosGroup, adaptPosLabel,
						new AcSpecies[]{weightPo},
						new AcSpecies[]{weight},
						new AcSpecies[]{inputCached},
						new Double[]{0.27437842255810574, 5.0});

			}
			acReactionSetDAO.save(reactionSetN);
			
			AcCompartment compartmentN = new AcCompartment();
			compartmentN.setCreatedBy(reactionSetN.getCreatedBy());
			compartmentN.setLabel(reactionSetLabel);
			compartmentN.setReactionSet(reactionSetN);
			acCompartmentDAO.save(compartmentN);
		}
		assertTrue(true);
	}

	private void addReaction(
		AcReactionSet reactionSet,
		AcReactionGroup reactionGroup,
		String label,
		AcSpecies[] reactants,
		AcSpecies[] products,
		AcSpecies[] catalysts,
		Double[] rateConstants
	) {
		AcReaction reaction = new AcReaction();
		reaction.setLabel(label);
		reaction.setForwardRateConstants(rateConstants);
		reaction.addAssociationsForSpecies(Arrays.asList(reactants), AcSpeciesAssociationType.Reactant);
		reaction.addAssociationsForSpecies(Arrays.asList(products), AcSpeciesAssociationType.Product);
		reaction.addAssociationsForSpecies(Arrays.asList(catalysts), AcSpeciesAssociationType.Catalyst);
		reactionSet.addReaction(reaction);
		reactionGroup.addReaction(reaction);
	}
}