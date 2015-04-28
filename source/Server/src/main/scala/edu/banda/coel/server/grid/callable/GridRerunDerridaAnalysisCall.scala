package edu.banda.coel.server.grid.callable

import com.banda.chemistry.business.ChemistryRunSetting
import com.banda.chemistry.domain.ArtificialChemistry
import com.banda.math.business.dynamics.JavaDoubleStatsSingleRunDynamicsAnalysis
import com.banda.math.domain.StatsSequence
import com.banda.math.domain.dynamics.SingleRunAnalysisResult
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcSimulationConfig
import com.banda.serverbase.grid.ArgumentCallable

/**
 * @author © Peter Banda
 * @since 2014
 */
class GridRerunDerridaAnalysisCall(
  chemistryRunnableFactory : ChemistryRunnableFactory,
	compartment : AcCompartment,
  simulationConfig : AcSimulationConfig,
	spec : SingleRunAnalysisSpec) extends ArgumentCallable[SingleRunAnalysisResult, StatsSequence] {

	val analysis = new JavaDoubleStatsSingleRunDynamicsAnalysis(spec)

	override def call(result : SingleRunAnalysisResult) : StatsSequence = {
	  val initialState = result.getInitialState
		val chemistryRunnable = chemistryRunnableFactory.createNonInteractive(compartment, simulationConfig, None)
		val	statsSequence = analysis.runDerridaOnly(chemistryRunnable, initialState)
		statsSequence.setId(result.getDerridaResults().getId())
		statsSequence
	}
}