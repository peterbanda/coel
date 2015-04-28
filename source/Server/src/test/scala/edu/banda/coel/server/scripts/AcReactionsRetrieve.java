package edu.banda.coel.server.scripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.ArtificialChemistryUtil;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionSet;
import com.banda.chemistry.domain.AcSpeciesAssociationType;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;


@Transactional
public class AcReactionsRetrieve extends CoelTest {

	private static int AC_REACTION_SET_ID = 1016;

	@Autowired
	@Qualifier(value="acReactionSetDAO")
	GenericDAO<AcReactionSet, Long> acReactionSetDAO;

	ArtificialChemistryUtil acUtil = ArtificialChemistryUtil.getInstance();

	@Test
	public void retrieveReactionsForTex() {
		AcReactionSet reactionSet = acReactionSetDAO.get(new Long(AC_REACTION_SET_ID));
		System.out.println(acUtil.getLatexTable(reactionSet, false));
		assertTrue(true);
	}

	@Test
	@Ignore
	public void retrieveReactions() {
		StringBuilder sb = new StringBuilder();
		AcReactionSet reactionSet = acReactionSetDAO.get(new Long(AC_REACTION_SET_ID));
		for (AcReaction reaction : reactionSet.getReactions()) {
			sb.append("reactionLabelSpeciesAssocsMaps.put(\"");
			sb.append(reaction.getLabel());
			sb.append("\"");
			sb.append(", new String[][] {{");
			sb.append(StringUtils.join(reaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant), "\","));
			sb.append("},{");
			sb.append(StringUtils.join(reaction.getSpeciesAssociations(AcSpeciesAssociationType.Product), "\","));
			sb.append("},{");
			sb.append(StringUtils.join(reaction.getSpeciesAssociations(AcSpeciesAssociationType.Catalyst), "\","));
			sb.append("},{");
			sb.append(StringUtils.join(reaction.getSpeciesAssociations(AcSpeciesAssociationType.Inhibitor), "\","));
			sb.append("}});\n");
		}
		System.out.println(sb.toString());
		assertTrue(true);
	}

	@Test
	@Rollback(false)
	@Ignore
	public void setSpeciesAssociations() {
		Map<String, String[][]> reactionLabelSpeciesAssocsMap = new HashMap<String, String[][]>();
		reactionLabelSpeciesAssocsMap.put("R01", new String[][] {{"W0po","E"},{"W0pox","Zpo"},{"X1v1","X1v0","X2v1","X2v0"},{}});
		reactionLabelSpeciesAssocsMap.put("R02", new String[][] {{"W0ne","E"},{"Zne","W0nex"},{"X2v0","X1v1","X1v0","X2v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R03", new String[][] {{"W0po","W0ne"},{},{},{}});
		reactionLabelSpeciesAssocsMap.put("R04", new String[][] {{"W1po"},{"W1pox"},{"X1v0"},{}});
		reactionLabelSpeciesAssocsMap.put("R05", new String[][] {{"E","W1po"},{"W1pox","Zpo"},{"X1v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R06", new String[][] {{"W1ne"},{"W1nex"},{"X1v0"},{}});
		reactionLabelSpeciesAssocsMap.put("R07", new String[][] {{"E","W1ne"},{"Zne","W1nex"},{"X1v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R08", new String[][] {{"W1ne","W1po"},{},{},{}});
		reactionLabelSpeciesAssocsMap.put("R09", new String[][] {{"W2po"},{"W2pox"},{"X2v0"},{}});
		reactionLabelSpeciesAssocsMap.put("R10", new String[][] {{"E","W2po"},{"W2pox","Zpo"},{"X2v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R11", new String[][] {{"W2ne"},{"W2nex"},{"X2v0"},{}});
		reactionLabelSpeciesAssocsMap.put("R12", new String[][] {{"E","W2ne"},{"Zne","W2nex"},{"X2v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R13", new String[][] {{"W2po","W2ne"},{},{},{}});
		reactionLabelSpeciesAssocsMap.put("R14", new String[][] {{"Zne","Zpo"},{},{},{}});
		reactionLabelSpeciesAssocsMap.put("R15", new String[][] {{"Zpo"},{"Yv1"},{},{"Zne"}});
		reactionLabelSpeciesAssocsMap.put("R16", new String[][] {{"Zne"},{"Yv0"},{},{"Zpo"}});
		reactionLabelSpeciesAssocsMap.put("R17", new String[][] {{"X1v0"},{},{},{"W1ne","W0po","W1po","W0ne","W2ne","W2po"}});
		reactionLabelSpeciesAssocsMap.put("R18", new String[][] {{"X1v1"},{},{},{"W0po","W1po","W0ne","W2ne","W2po","W1ne"}});
		reactionLabelSpeciesAssocsMap.put("R19", new String[][] {{"X2v0"},{},{},{"W1po","W0ne","W2ne","W2po","W1ne","W0po"}});
		reactionLabelSpeciesAssocsMap.put("R20", new String[][] {{"X2v1"},{},{},{"W0ne","W1po","W0po","W1ne","W2po","W2ne"}});
		reactionLabelSpeciesAssocsMap.put("R21", new String[][] {{"W0pox"},{"W0po"},{},{"X1v0","X1v1","X2v1","X2v0"}});
		reactionLabelSpeciesAssocsMap.put("R22", new String[][] {{"W0nex"},{"W0ne"},{},{"X1v0","X1v1","X2v1","X2v0"}});
		reactionLabelSpeciesAssocsMap.put("R23", new String[][] {{"W1pox"},{"W1po"},{},{"X2v0","X1v1","X1v0","X2v1"}});
		reactionLabelSpeciesAssocsMap.put("R24", new String[][] {{"W1nex"},{"W1ne"},{},{"X1v1","X1v0","X2v1","X2v0"}});
		reactionLabelSpeciesAssocsMap.put("R25", new String[][] {{"W2pox"},{"W2po"},{},{"X1v1","X1v0","X2v1","X2v0"}});
		reactionLabelSpeciesAssocsMap.put("R26", new String[][] {{"W2nex"},{"W2ne"},{},{"X1v1","X1v0","X2v1","X2v0"}});
		reactionLabelSpeciesAssocsMap.put("R27", new String[][] {{"Yv0"},{},{},{"X2v1","X2v0","X1v1","X1v0"}});
		reactionLabelSpeciesAssocsMap.put("R28", new String[][] {{"Yv1"},{},{},{"X2v0","X2v1","X1v1","X1v0"}});
		reactionLabelSpeciesAssocsMap.put("R29", new String[][] {{"Dv0"},{},{},{"X2v0","X2v1","X1v1","X1v0"}});
		reactionLabelSpeciesAssocsMap.put("R30", new String[][] {{"Dv1"},{},{},{"X2v0","X2v1","X1v1","X1v0"}});
		reactionLabelSpeciesAssocsMap.put("R31", new String[][] {{"Dv0"},{"W0ne"},{"Yv1"},{}});
		reactionLabelSpeciesAssocsMap.put("R32", new String[][] {{"Dv0"},{"W1ne"},{"Yv1","X1v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R33", new String[][] {{"Dv0"},{"W2ne"},{"Yv1","X2v1"},{}});
		reactionLabelSpeciesAssocsMap.put("R34", new String[][] {{"Dv1"},{"W0po"},{"Yv0"},{}});
		reactionLabelSpeciesAssocsMap.put("R35", new String[][] {{"Dv1"},{"W1po"},{"X1v1","Yv0"},{}});
		reactionLabelSpeciesAssocsMap.put("R36", new String[][] {{"Dv1"},{"W2po"},{"X2v1","Yv0"},{}});
		AcReactionSet reactionSet = acReactionSetDAO.get(new Long(AC_REACTION_SET_ID));
		for (AcReaction reaction : reactionSet.getReactions()) {
			String[][] speciesAssociationLabels = reactionLabelSpeciesAssocsMap.get(reaction.getLabel());
			acUtil.updateSpeciesAssociations(StringUtils.join(speciesAssociationLabels[0], ","), AcSpeciesAssociationType.Reactant, reaction);
			acUtil.updateSpeciesAssociations(StringUtils.join(speciesAssociationLabels[1], ","), AcSpeciesAssociationType.Product, reaction);
			acUtil.updateSpeciesAssociations(StringUtils.join(speciesAssociationLabels[2], ","), AcSpeciesAssociationType.Catalyst, reaction);
			acUtil.updateSpeciesAssociations(StringUtils.join(speciesAssociationLabels[3], ","), AcSpeciesAssociationType.Inhibitor, reaction);
		}
		assertTrue(true);
	}

//	private Collection<AcSpeciesReactionAssociation> createSpeciesReactionAssociations(String[] speciesAssociationLabels, AcSpeciesAssociationType type) {
//		Collection<AcSpeciesReactionAssociation> speciesAssocs = new ArrayList<AcSpeciesReactionAssociation>();
//		for (String label : speciesAssociationLabels) {
//			speciesAssocs.add(new AcSpeciesReactionAssociation(species, stoichiometricFactor));
//		}
//	}
}