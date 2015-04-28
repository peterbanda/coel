package edu.banda.coel.business;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.BeanUtils;

import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.chemistry.domain.AcInteractionVariableAssignment;
import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionGroup;
import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.core.util.ObjectUtil;
import com.banda.function.domain.AbstractFunction;
import com.banda.network.domain.NetworkActionSeries;

import edu.banda.coel.domain.evo.AcInteractionVariableAssignmentBound;
import edu.banda.coel.domain.evo.AcSpeciesAssignmentBound;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;
import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.domain.evo.EvoNetworkTask;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class Replicator {

	private static Replicator instance = new Replicator();

	private Replicator() {
		// nothing to do
	}

	public static Replicator getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T clone(T object) {
		if (object == null) {
			return null;
		}
		T copiedObject = null;
		if (object.getClass().isArray()) {
			copiedObject = (T) ((Object[]) object).clone();;			
		} else {
			copiedObject = (T) BeanUtils.instantiate(object.getClass());
			BeanUtils.copyProperties(object, copiedObject);
		}
		return copiedObject;
	}

	public EvoAcRateConstantTask cloneEvoAcRateConstantTask(EvoAcRateConstantTask evoAcTask) {
		EvoAcRateConstantTask evoAcTaskClone = clone(evoAcTask);
		evoAcTaskClone.initRateConstantTypeBounds();
		for (AcRateConstantTypeBound bound : evoAcTask.getRateConstantTypeBounds()) {
			evoAcTaskClone.addRateConstantTypeBound(clone(bound));
		}
		evoAcTaskClone.initActionSeries();
		for (AcInteractionSeries actionSeries: evoAcTask.getActionSeries()) {
			evoAcTaskClone.addActionSeries(actionSeries);
		}
		evoAcTaskClone.setFixedRateReactions(new ArrayList<AcReaction>());
		for (AcReaction reaction : evoAcTask.getFixedRateReactions()) {
			evoAcTaskClone.addFixedRateReaction(reaction);
		}
		evoAcTaskClone.setFixedRateReactionGroups(new ArrayList<AcReactionGroup>());
		for (AcReactionGroup reactionGroup : evoAcTask.getFixedRateReactionGroups()) {
			evoAcTaskClone.addFixedRateReactionGroup(reactionGroup);
		}
		evoAcTaskClone.setEvolutionRuns(null);
		return evoAcTaskClone;
	}

	public void nullIdAndVersion(EvoAcInteractionSeriesTask evoAcTask) {
		ObjectUtil.nullIdAndVersion(evoAcTask);
		for (AcSpeciesAssignmentBound bound : evoAcTask.getSpeciesAssignmentBounds()) {
			ObjectUtil.nullIdAndVersion(bound);
		}
		for (AcInteractionVariableAssignmentBound bound : evoAcTask.getVariableAssignmentBounds()) {
			ObjectUtil.nullIdAndVersion(bound);
		}		
	}

	public EvoAcInteractionSeriesTask cloneEvoAcInteractionSeriesTask(EvoAcInteractionSeriesTask evoAcTask) {
		EvoAcInteractionSeriesTask evoAcTaskClone = clone(evoAcTask);
		evoAcTaskClone.setSpeciesAssignmentBounds(new HashSet<AcSpeciesAssignmentBound>());
		for (AcSpeciesAssignmentBound bound : evoAcTask.getSpeciesAssignmentBounds()) {
			AcSpeciesAssignmentBound boundClone = clone(bound);
			boundClone.setAssignments(new HashSet<AcSpeciesInteraction>(bound.getAssignments()));
			evoAcTaskClone.addSpeciesAssignmentBound(boundClone);
		}
		evoAcTaskClone.setVariableAssignmentBounds(new HashSet<AcInteractionVariableAssignmentBound>());
		for (AcInteractionVariableAssignmentBound bound : evoAcTask.getVariableAssignmentBounds()) {
			AcInteractionVariableAssignmentBound boundClone = clone(bound);
			boundClone.setAssignments(new HashSet<AcInteractionVariableAssignment>(bound.getAssignments()));
			evoAcTaskClone.addVariableAssignmentBound(boundClone);
		}
		evoAcTaskClone.initActionSeries();
		for (AcInteractionSeries actionSeries: evoAcTask.getActionSeries()) {
			evoAcTaskClone.addActionSeries(actionSeries);
		}
		evoAcTaskClone.setEvolutionRuns(null);
		return evoAcTaskClone;
	}

	public <T> EvoNetworkTask<T> cloneEvoNetworkTask(EvoNetworkTask<T> evoNetworkTask) {
		EvoNetworkTask<T> evoNetworkTaskClone = clone(evoNetworkTask);

		evoNetworkTaskClone.setActionSeries(new HashSet<NetworkActionSeries<T>>());
		for (NetworkActionSeries<T> actionSeries: evoNetworkTask.getActionSeries()) {
			evoNetworkTaskClone.addActionSeries(actionSeries);
		}

		evoNetworkTaskClone.setEvolutionRuns(null);
		return evoNetworkTaskClone;
	}
}