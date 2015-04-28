package edu.banda.coel.server.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.chemistry.business.AcRateConstantUtil;
import com.banda.chemistry.domain.*;
import com.banda.chemistry.domain.AcReaction.ReactionDirection;
import com.banda.core.domain.ValueBound;
import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.server.CoelTest;

@Transactional
public class AcRateConstantSettingTest extends CoelTest {

//	private static final Long COMPARTMENT_ID = 3456l;

	private static final Long COMPARTMENT_ID = 1017l;

	private static final Long REACTION_GROUP_TO_OMIT_ID = 1024l;

	@Autowired
	GenericDAO<AcCompartment, Long> acCompartmentDAO;

	@Autowired
	GenericDAO<AcReactionGroup, Long> acReactionGroupDAO;	

	private final AcRateConstantUtil acRateConstantUtil = AcRateConstantUtil.getInstance();

	@Test
	public void testRateConstantGetAndSet() {
		final AcReactionGroup reactionGroup = acReactionGroupDAO.get(REACTION_GROUP_TO_OMIT_ID);

		final AcCompartment compartment = acCompartmentDAO.get(COMPARTMENT_ID);
		Collection<ValueBound<Double>> valueBounds = acRateConstantUtil.getRateConstantBounds(
				compartment, AcRateConstantType.getDefaultRateConstantBoundMap(), ReactionDirection.Both,
				null, Collections.singleton(reactionGroup));

		List<Double> rateConstants = new ArrayList<Double>();
		for (ValueBound<Double> valueBound : valueBounds) {
			System.out.println(valueBound.getFrom() + " - " + valueBound.getTo());
			rateConstants.add(new Double(rateConstants.size())); 
		}
		acRateConstantUtil.setRateConstants(compartment, rateConstants.toArray(new Double[0]), ReactionDirection.Both, null, Collections.singleton(reactionGroup));
		reportCompartmentsRecursively(compartment);
	}

	private void reportCompartmentsRecursively(AcCompartment compartment) {
		System.out.println("Compartment : " + compartment.getLabel());
		System.out.println("Reaction Set : " + compartment.getReactionSet().getLabel());
		for (AcReaction reaction : compartment.getReactionSet().getReactions()) {
			System.out.println(reaction.getLabel() + " : " + Arrays.toString(reaction.getForwardRateConstants()));
		}
		System.out.println("Channels");
		for (AcCompartmentChannel channel : compartment.getChannels()) {
			System.out.println(channel.getSourceSpecies().getLabel() + " -> " + channel.getTargetSpecies().getLabel() + " : " + channel.getPermeability());
		}
		for (AcCompartment subCompartment : compartment.getSubCompartments()) {
			reportCompartmentsRecursively(subCompartment);
		}
	}
}