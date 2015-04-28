package edu.banda.coel.domain.rc;

import com.banda.chemistry.domain.AcReactionSetConstraints;
import com.banda.chemistry.domain.AcSpeciesToReactionConstraints;
import com.banda.math.domain.rand.RandomDistribution;

import edu.banda.coel.CoelRuntimeException;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class RcAcMachineTemplate extends RcMachineTemplate {

	private AcReactionSetConstraints reservoirReactionConstraints;
	private AcReactionSetConstraints inputToReservoirReactionConstraints;
	private AcReactionSetConstraints reservoirToReadoutReactionConstraints;
	private RandomDistribution<Double> initFeedbackWeightDistribution;
	private Integer weightNodesNum;

	public AcReactionSetConstraints getReservoirReactionConstraints() {
		return reservoirReactionConstraints;
	}

	public void setReservoirReactionConstraints(AcReactionSetConstraints reservoirReactionConstraints) {
		this.reservoirReactionConstraints = reservoirReactionConstraints;
	}

	public AcReactionSetConstraints getInputToReservoirReactionConstraints() {
		return inputToReservoirReactionConstraints;
	}

	public void setInputToReservoirReactionConstraints(AcReactionSetConstraints inputToReservoirReactionConstraints) {
		this.inputToReservoirReactionConstraints = inputToReservoirReactionConstraints;
	}

	public AcReactionSetConstraints getReservoirToReadoutReactionConstraints() {
		return reservoirToReadoutReactionConstraints;
	}

	public void setReservoirToReadoutReactionConstraints(AcReactionSetConstraints reservoirToReadoutReactionConstraints) {
		this.reservoirToReadoutReactionConstraints = reservoirToReadoutReactionConstraints;
	}

	@Override
	public RcMachineType getType() {
		return RcMachineType.ArtificialChemistry;
	}

	private AcSpeciesToReactionConstraints getInputToReservoirStoRConstraints() {
		return inputToReservoirReactionConstraints.getSpeciesToReactionConstraints();
	}

	private AcSpeciesToReactionConstraints getReservoirStoRConstraints() {
		return reservoirReactionConstraints.getSpeciesToReactionConstraints();
	}

	private AcSpeciesToReactionConstraints getReservoirToReadoutStoRConstraints() {
		return reservoirToReadoutReactionConstraints.getSpeciesToReactionConstraints();
	}

	@Override
	public Integer getInputToReservoirFixedOutDegree() {
		return getInputToReservoirStoRConstraints().getFixedReactantAssocsNum();
	}

	@Override
	public void setInputToReservoirFixedOutDegree(Integer inputToReservoirFixedOutDegree) {
		getReservoirToReadoutStoRConstraints().setFixedReactantAssocsNum(inputToReservoirFixedOutDegree);
	}

	@Override
	public Integer getReservoirFixedInDegree() {
		return getReservoirToReadoutStoRConstraints().getFixedProductAssocsNum();
	}

	@Override
	public void setReservoirFixedInDegree(Integer reservoirFixedInDegree) {
		getReservoirStoRConstraints().setFixedProductAssocsNum(reservoirFixedInDegree);
	}

	@Override
	public Integer getReservoirToReadoutFixedOutDegree() {
		return getInputToReservoirStoRConstraints().getFixedReactantAssocsNum();
	}

	@Override
	public void setReservoirToReadoutFixedOutDegree(Integer reservoirToReadoutFixedOutDegree) {
		getInputToReservoirStoRConstraints().setFixedReactantAssocsNum(reservoirToReadoutFixedOutDegree);
	}

	@Override
	public Integer getReadoutToReservoirFixedOutDegree() {
		throw new CoelRuntimeException("AC type of reservoir computing does not support readout to reservoir connections.");
	}

	@Override
	public void setReadoutToReservoirFixedOutDegree(Integer readoutToReservoirFixedOutDegree) {
		throw new CoelRuntimeException("AC type of reservoir computing does not support readout to reservoir connections.");
	}

	public Integer getWeightNodesNum() {
		return weightNodesNum;
	}

	public void setWeightNodesNum(Integer weightNodesNum) {
		this.weightNodesNum = weightNodesNum;
	}

	public RandomDistribution<Double> getInitFeedbackWeightDistribution() {
		return initFeedbackWeightDistribution;
	}

	public void setInitFeedbackWeightDistribution(RandomDistribution<Double> initFeedbackWeightDistribution) {
		this.initFeedbackWeightDistribution = initFeedbackWeightDistribution;
	}
}