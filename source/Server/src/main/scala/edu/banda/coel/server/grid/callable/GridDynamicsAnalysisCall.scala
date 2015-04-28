package edu.banda.coel.server.grid.callable

import java.{util => ju,lang => jl}
import com.banda.chemistry.business.ChemistryRunSetting
import com.banda.chemistry.domain.ArtificialChemistry
import com.banda.math.business.dynamics.JavaDoubleStatsSingleRunDynamicsAnalysis
import com.banda.math.domain.dynamics.SingleRunAnalysisResult
import com.banda.math.domain.dynamics.SingleRunAnalysisSpec
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.core.domain.um.User
import com.banda.chemistry.domain.AcSimulationConfig
import com.banda.chemistry.domain.AcCompartment
import com.banda.serverbase.grid.ArgumentCallable

/**
 * @author © Peter Banda
 * @since 2012
 */
class GridDynamicsAnalysisCall(
    chemistryRunnableFactory : ChemistryRunnableFactory,
	compartment : AcCompartment,
    simulationConfig : AcSimulationConfig,
	spec : SingleRunAnalysisSpec) extends ArgumentCallable[ju.List[jl.Double], SingleRunAnalysisResult] {

  private val analysis = new JavaDoubleStatsSingleRunDynamicsAnalysis(spec)

	override def call(initialState : ju.List[jl.Double]) : SingleRunAnalysisResult = {
		val chemistryRunnable = chemistryRunnableFactory.createNonInteractive(compartment, simulationConfig, None)
		val result = analysis.run(chemistryRunnable, initialState)
		val user = new User()
		user.setId(1l)
		result.setCreatedBy(user)
		result
	}
}