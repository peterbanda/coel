package edu.banda.coel.business.rc;

import java.util.ArrayList;
import java.util.Collection;

import com.banda.chemistry.domain.AcReactionSetConstraints;

import edu.banda.coel.core.CoreTestDataGenerator;
import edu.banda.coel.domain.rc.RcAcMachineTemplate;
import edu.banda.coel.domain.rc.RcAcSetting;

/**
 * @author © Peter Banda
 * @since 2013
 */
@Deprecated
public class RcRunBOTestDataGenerator extends CoreTestDataGenerator {

	private Collection<AcReactionSetConstraints> reservoirReactionConstraints = new ArrayList<AcReactionSetConstraints>();
	private Collection<RcAcMachineTemplate> rcAcMachineTemplates = new ArrayList<RcAcMachineTemplate>();
	private Collection<RcAcMachineTemplate> realRcAcMachineTemplates = new ArrayList<RcAcMachineTemplate>();
	private Collection<RcAcSetting> rcAcSettings = new ArrayList<RcAcSetting>();
	private Collection<RcAcSetting> rcAcRealSettings = new ArrayList<RcAcSetting>();

//	private AcCoreTestDataGenerator acTestDataGenerator = new AcCoreTestDataGenerator();

//	public RcRunBOTestDataGenerator() {
//		setUpTestData();
//	}
//
//	private void setUpTestData() {
//		setReservoirReactionConstraints();
//		setRcMachineTemplates();
//		setRealRcMachineTemplates();
//		setRcAcSettings();
//		setRcAcRealSettings();
//	}
//
//	private void setReservoirReactionConstraints() {
//		// instance 1
//		AcReactionSetConstraints reservoirConstraints1 = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints reservoirRToSConstraints1 = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints reservoirSToRConstraints1 = new AcSpeciesToReactionConstraints();
//		reservoirRToSConstraints1.setMinReactantsNum(1);
//		reservoirRToSConstraints1.setMinProductsNum(1);
//		reservoirRToSConstraints1.setMaxReactantsNum(2);
//		reservoirRToSConstraints1.setMaxProductsNum(2);
//		reservoirRToSConstraints1.setMaxCatalystsNum(1);
//		reservoirRToSConstraints1.setMaxInhibitorsNum(0);
//
//		reservoirSToRConstraints1.setFixedProductAssocsNum(2);
//		reservoirSToRConstraints1.setFixedCatalystAssocsNum(1);
//		
//		reservoirConstraints1.setReactionToSpeciesConstraints(reservoirRToSConstraints1);
//		reservoirConstraints1.setSpeciesToReactionConstraints(reservoirSToRConstraints1);
//		reservoirConstraints1.setReactionsPerSpeciesRatio(1.2d);
//		reservoirReactionConstraints.add(reservoirConstraints1);
//
//		// instance 2
//		AcReactionSetConstraints reservoirConstraints2 = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints reservoirRToSConstraints2 = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints reservoirSToRConstraints2 = new AcSpeciesToReactionConstraints();
//		reservoirRToSConstraints2.setMinReactantsNum(1);
//		reservoirRToSConstraints2.setMinProductsNum(1);
//		reservoirRToSConstraints2.setMaxReactantsNum(1);
//		reservoirRToSConstraints2.setMaxProductsNum(1);
//		reservoirRToSConstraints2.setMinCatalystsNum(1);
//		reservoirRToSConstraints2.setMaxCatalystsNum(1);
//		reservoirRToSConstraints2.setMaxInhibitorsNum(0);
//
//		reservoirSToRConstraints2.setFixedReactantAssocsNum(2);
//		
//		reservoirConstraints2.setReactionToSpeciesConstraints(reservoirRToSConstraints2);
//		reservoirConstraints2.setSpeciesToReactionConstraints(reservoirSToRConstraints2);
//		reservoirConstraints2.setReactionsPerSpeciesRatio(2d);
//		reservoirReactionConstraints.add(reservoirConstraints2);
//
//		// instance 3
//		AcReactionSetConstraints reservoirConstraints3 = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints reservoirRToSConstraints3 = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints reservoirSToRConstraints3 = new AcSpeciesToReactionConstraints();
//		reservoirRToSConstraints3.setMaxReactantsNum(2);
//		reservoirRToSConstraints3.setMaxProductsNum(2);
//		reservoirRToSConstraints3.setMinReactantsNum(1);
//		reservoirRToSConstraints3.setMaxCatalystsNum(1);
//		reservoirRToSConstraints3.setMinInhibitorsNum(1);
//
//		reservoirSToRConstraints3.setFixedProductAssocsNum(2);
//		reservoirSToRConstraints3.setFixedCatalystAssocsNum(1);
//		
//		reservoirConstraints3.setReactionToSpeciesConstraints(reservoirRToSConstraints3);
//		reservoirConstraints3.setSpeciesToReactionConstraints(reservoirSToRConstraints3);
//		reservoirConstraints3.setReactionsPerSpeciesRatio(1.2d);
//		reservoirReactionConstraints.add(reservoirConstraints3);
//	}
//
//	private void setRcMachineTemplates() {
//		for (AcReactionSetConstraints reservoirConstraints : reservoirReactionConstraints) {
//			RcAcMachineTemplate rcMachineTemplate = new RcAcMachineTemplate();
//			rcMachineTemplate.setReservoirReactionConstraints(reservoirConstraints);
//			rcMachineTemplate.setInputToReservoirReactionConstraints(createInputToReservoirReactionConstraints());
//			rcMachineTemplate.setReservoirToReadoutReactionConstraints(createReservoirToReadoutReactionConstraints());
//			rcMachineTemplate.setReservoirNodesNum(getRandomInt(10, 30));
//			rcMachineTemplate.setInputNodesNum(getRandomInt(1, 3));
//			if (getRandomBoolean()) {
//				rcMachineTemplate.setReadoutNodesNum(getRandomInt(1, 3));
//				rcMachineTemplate.setWeightNodesNum(0);
//			} else {
//				rcMachineTemplate.setReadoutNodesNum(2);
//				rcMachineTemplate.setWeightNodesNum(2 * getRandomInt(1, 4));				
//			}
//			rcMachineTemplate.setUseFullInputToReservoirConnections(false);
//			rcMachineTemplate.setUseFullReservoirToReadoutConnections(false);
//			rcMachineTemplate.setUseFullReadoutToReservoirConnections(false);
//			rcMachineTemplate.setInitReservoirWeightDistribution(createRandomDistribution());
//			rcMachineTemplate.setInitFeedbackWeightDistribution(createRandomDistribution());
//
//			rcAcMachineTemplates.add(rcMachineTemplate);
//		}
//	}
//
//	private AcReactionSetConstraints createRealReservoirConstraints() {
//		AcReactionSetConstraints reservoirConstraints1 = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints reservoirRToSConstraints1 = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints reservoirSToRConstraints1 = new AcSpeciesToReactionConstraints();
//		reservoirRToSConstraints1.setMinReactantsNum(1);
//		reservoirRToSConstraints1.setMinProductsNum(1);
//		reservoirRToSConstraints1.setMaxReactantsNum(2);
//		reservoirRToSConstraints1.setMaxProductsNum(2);
//		reservoirRToSConstraints1.setMaxCatalystsNum(1);
//		reservoirRToSConstraints1.setMaxInhibitorsNum(0);
//
//		reservoirSToRConstraints1.setFixedProductAssocsNum(2);
//		reservoirSToRConstraints1.setFixedCatalystAssocsNum(1);
//		
//		reservoirConstraints1.setReactionToSpeciesConstraints(reservoirRToSConstraints1);
//		reservoirConstraints1.setSpeciesToReactionConstraints(reservoirSToRConstraints1);
//		reservoirConstraints1.setReactionsPerSpeciesRatio(1.2d);
//		return reservoirConstraints1;
//	}
//
//	private void setRealRcMachineTemplates() {
//			RcAcMachineTemplate rcMachineTemplate = new RcAcMachineTemplate();
//			rcMachineTemplate.setReservoirReactionConstraints(createRealReservoirConstraints());
//			rcMachineTemplate.setInputToReservoirReactionConstraints(createInputToReservoirReactionConstraints());
//			rcMachineTemplate.setReservoirToReadoutReactionConstraints(createReservoirToReadoutReactionConstraints());
//			rcMachineTemplate.setReservoirNodesNum(getRandomInt(10, 30));
//			rcMachineTemplate.setInputNodesNum(2);
//			rcMachineTemplate.setReadoutNodesNum(2);
//			rcMachineTemplate.setWeightNodesNum(2 * getRandomInt(5, 10));				
//			rcMachineTemplate.setUseFullInputToReservoirConnections(false);
//			rcMachineTemplate.setUseFullReservoirToReadoutConnections(false);
//			rcMachineTemplate.setUseFullReadoutToReservoirConnections(false);
//			rcMachineTemplate.setInitReservoirWeightDistribution(new UniformRandomDistribution<Double>(0.2d, 0.5d));
//			rcMachineTemplate.setInitFeedbackWeightDistribution(new UniformRandomDistribution<Double>(2d, 10d));
//			realRcAcMachineTemplates.add(rcMachineTemplate);
//	}
//
//	private void setRcAcSettings() {
//		for (int i = 0; i < 50; i++) {
//			for (RcAcMachineTemplate rcAcMachineTemplate : rcAcMachineTemplates) {
//				RcAcSetting rcAcSetting = new RcAcSetting();
//				rcAcSetting.setRcMachineTemplate(rcAcMachineTemplate);
//				rcAcSetting.setTaskType(getRandomBooleanFunctionType());
//				rcAcSetting.setTimeWindowLength(getRandomInt(4, 10));
//				rcAcSetting.setInputsNumPerInputStream(getRandomInt(50, 100));
//
//				rcAcSetting.setActionInterval(getRandomInt(300, 2000));
//				rcAcSetting.setAfterInitStartingTimeStep(20);
//				rcAcSetting.setInputConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getInputNodesNum()));
//				rcAcSetting.setOutputConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getReadoutNodesNum()));
//				if (getRandomBoolean()) {
//					rcAcSetting.setFeedbackConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getReadoutNodesNum()));
//				}
//				rcAcSetting.setInputSpeciesStateTransFunction(getRandomAggregateFunction());
//				rcAcSetting.setOutputSpeciesStateTransFunction(getRandomAggregateFunction());
//				if (rcAcSetting.isFeedbackMode()) {
//					rcAcSetting.setFeedbackSpeciesStateTransFunction(getRandomAggregateFunction());
//				}
//				rcAcSetting.setInternalSpeciesStateTransFunction(getRandomAggregateFunction());
//				rcAcSetting.setReservoirTranslationMode(getRandomBoolean());
//				rcAcSettings.add(rcAcSetting);
//			}
//		}
//	}
//
//	private void setRcAcRealSettings() {
//		for (int i = 0; i < 50; i++) {
//			for (RcAcMachineTemplate rcAcMachineTemplate : realRcAcMachineTemplates) {
//				RcAcSetting rcAcSetting = new RcAcSetting();
//				rcAcSetting.setRcMachineTemplate(rcAcMachineTemplate);
//				rcAcSetting.setTaskType(BooleanFunctionType.Parity);
//				rcAcSetting.setTimeWindowLength(3);
//				rcAcSetting.setInputsNumPerInputStream(getRandomInt(50, 100));
//
//				rcAcSetting.setActionInterval(getRandomInt(500, 2000));
//				rcAcSetting.setAfterInitStartingTimeStep(1);
//				rcAcSetting.setInputConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getInputNodesNum()));
//				rcAcSetting.setOutputConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getReadoutNodesNum()));
//				rcAcSetting.setFeedbackConcentrationLevel(acTestDataGenerator.createConcentrationLevel(rcAcMachineTemplate.getReadoutNodesNum()));
////				rcAcSetting.setInputSpeciesStateTransFunction(getRandomAggregateFunctionBut(new AggregateFunction[] {AggregateFunction.Min, AggregateFunction.Avg, AggregateFunction.Last}));
////				rcAcSetting.setOutputSpeciesStateTransFunction(getRandomAggregateFunctionBut(new AggregateFunction[] {AggregateFunction.Min, AggregateFunction.First}));
////				rcAcSetting.setFeedbackSpeciesStateTransFunction(getRandomAggregateFunctionBut(new AggregateFunction[] {AggregateFunction.Min, AggregateFunction.Last}));
//				rcAcSetting.setInputSpeciesStateTransFunction(AggregateFunction.First);
//				rcAcSetting.setOutputSpeciesStateTransFunction(AggregateFunction.Last);
//				rcAcSetting.setFeedbackSpeciesStateTransFunction(AggregateFunction.First);
//				rcAcSetting.setInternalSpeciesStateTransFunction(getRandomAggregateFunction());
//				rcAcSetting.setReservoirTranslationMode(getRandomBoolean());
//				rcAcRealSettings.add(rcAcSetting);
//			}
//		}
//	}
//
//	private AcReactionSetConstraints createInputToReservoirReactionConstraints() {
//		AcReactionSetConstraints inputToReservoirReactionConstraints = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints inputRToSConstraints = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints inputSToRConstraints = new AcSpeciesToReactionConstraints();
//        inputRToSConstraints.setMinReactantsNum(1);
//        inputRToSConstraints.setMinProductsNum(1);
//        inputRToSConstraints.setMaxReactantsNum(1);
//        inputRToSConstraints.setMaxProductsNum(1);
//        inputSToRConstraints.setFixedReactantAssocsNum(getRandomInt(1, 4));
//        inputToReservoirReactionConstraints.setReactionToSpeciesConstraints(inputRToSConstraints);
//        inputToReservoirReactionConstraints.setSpeciesToReactionConstraints(inputSToRConstraints);
//        inputToReservoirReactionConstraints.setReactionsPerSpeciesRatioAssocType(AcSpeciesAssociationType.Reactant);
//        inputToReservoirReactionConstraints.setReactionsPerSpeciesRatio(inputSToRConstraints.getFixedReactantAssocsNum().doubleValue());
//        return inputToReservoirReactionConstraints;
//	}
//
//	private AcReactionSetConstraints createReservoirToReadoutReactionConstraints() {
//		AcReactionSetConstraints reservoirToReservoirReactionConstraints = new AcReactionSetConstraints();
//		AcReactionToSpeciesConstraints readoutRToSConstraints = new AcReactionToSpeciesConstraints();
//		AcSpeciesToReactionConstraints readoutSToRConstraints = new AcSpeciesToReactionConstraints();
//        readoutRToSConstraints.setMinReactantsNum(1);
//        readoutRToSConstraints.setMinProductsNum(1);
//        readoutRToSConstraints.setMaxReactantsNum(1);
//        readoutRToSConstraints.setMaxProductsNum(1);
//        readoutSToRConstraints.setFixedProductAssocsNum(getRandomInt(1, 4));
//        reservoirToReservoirReactionConstraints.setReactionToSpeciesConstraints(readoutRToSConstraints);
//        reservoirToReservoirReactionConstraints.setSpeciesToReactionConstraints(readoutSToRConstraints);
//        reservoirToReservoirReactionConstraints.setReactionsPerSpeciesRatioAssocType(AcSpeciesAssociationType.Product);
//        reservoirToReservoirReactionConstraints.setReactionsPerSpeciesRatio(readoutSToRConstraints.getFixedProductAssocsNum().doubleValue());
//        return reservoirToReservoirReactionConstraints;
//	}
//
//	protected Collection<RcAcSetting> getRcAcSettings() {
//		return rcAcSettings;
//	}
//
//	protected Collection<RcAcSetting> getRcAcRealSettings() {
//		return rcAcRealSettings;
//	}
}