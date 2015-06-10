import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

def notLoggedIn = { ->
   springSecurityService.principal instanceof String
}
def loggedIn = { ->
   !(springSecurityService.principal instanceof String)
}
def isAdmin = { ->
   SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')
}

navigation = {
	// Declare the "app" scope, used by default in tags
	app {

		chemistry(controller:'ac', action:'index', data:[icon:'dna']) {
			ChemistryHome(titleText: "Chemistry", action:'index', data:[icon:'icon-home'])
			Model(data:[header:'true'])
//			ArtificialChemistry(controller:'artificialChemistry', action:'list')
			Compartment(controller:'acCompartment', action:'list')
			ReactionSet(controller:'acReactionSet', action:'list')
			SpeciesSet(controller:'acSpeciesSet', action:'list')
			SimulationConfig(controller:'acSimulationConfig', action:'list')
			Interaction(data:[header:'true'])			
			InteractionSeries(controller:'acInteractionSeries', action:'list')
			TranslationSeries(controller:'acTranslationSeries', action:'list')
			Launch(data:[header:'true'])
			LaunchChemistry(titleText:"Chemistry", controller:'acRun', action:'index', data:[icon:'icon-play-circle'])
			PerformanceHeader(titleText:"Performance", data:[header:'true'])
			Evaluation(controller:'acEvaluation', action:'list')
			Performance(controller:'acEvaluatedPerformance', action:'list')
			RandomRatePerformance(controller:'acRandomRatePerformance', action:'list')
			PerturbationPerformance(controller:'acPerturbationPerformance', action:'list')
			RandomChemistry(data:[header:'true'])
			ArtificialChemistrySpec(controller:'artificialChemistrySpec', action:'list')
			Analysis(data:[header:'true'])
			SingleRunAnalysisSpec(controller:'singleRunAnalysisSpec', action:'list')
			MultiRunAnalysisSpec(controller:'multiRunAnalysisSpec', action:'list')
			SingleRunAnalysisResult(controller:'singleRunAnalysisResult', action:'list')
			MultiRunAnalysisResult(controller:'multiRunAnalysisResult', action:'list')
			Other(data:[header:'true'])
			RateConstantTypeBound(controller:'acRateConstantTypeBound', action:'list')
        }

		network(controller:'net', action:'index', data:[icon:'net']) {
			NetworkHome(titleText: "Network", action:'index', data:[icon:'icon-home'])
			Model(data:[header:'true'])
			Network(controller:'network', action:'list')
			Topology(controller:'topology', action:'list')
			SpatialNeighborhood(controller:'spatialNeighborhood', action:'list')
			Function(controller:'networkFunction', action:'list')
			WeightSetting(controller:'networkWeightSetting', action:'list')
			Interaction(data:[header:'true'])
			InteractionSeries(controller:'networkActionSeries', action:'list')
			Launch(data:[header:'true'])
			LaunchNetwork(controller:'networkRun', action:'index', data:[icon:'icon-play-circle'])
			PerformanceHeader(titleText:"Performance", data:[header:'true'])
			Evaluation(controller:'networkEvaluation', action:'list')
			Performance(controller:'networkPerformance', action:'list')
			SpatialPerformance(controller:'spatialNetworkPerformance', action:'list')
			Analysis(data:[header:'true'])
			DerridaAnalysis(controller:'networkDerridaAnalysis', action:'list')
			DamageSpreading(controller:'networkDamageSpreading', action:'list')
		}

		evolution(controller:'evo', action:'index', data:[icon:'evo']) {
			EvolutionHome(titleText: "Evolution", action:'index', data:[icon:'icon-home'])
			Model(data:[header:'true'])
			GASetting(controller:'evoGaSetting', action:'list', titleText:'GA Setting')
			EvolutionTask(controller:'evoTask', action:'list')
			Launch(data:[header:'true'])
			EvolutionRun(controller:'evoRun', action:'list', data:[icon:'icon-play-circle'])
			Other(data:[header:'true'])
			ArtificialChemistrySpecBound(controller:'artificialChemistrySpecBound', action:'list', titleText:'AC Spec Bound')
			Task(controller:'task', action:'list')
		}

		chemPic(controller:'chemPic', action:'index')

        download(controller:'download', action:'index', data:[icon:'dna']) {
            DownloadHome(titleText: "Download", action:'index', data: [icon: 'icon-home'])
			Model(data:[header:'true'])
            Chemistry(action: 'chemistry')
            Network(action: 'network')
        }

		// Items pointing to ContentController, using the specific action
//		about(controller:'content')
//		contact(controller:'content')
//		help(controller:'content')
	}

//	user {
//		login visible: notLoggedIn
//		logout visible: loggedIn
//		viewProfile(controller:'user', action:'showLoggedUser', visible: loggedIn)
//	}
//
//	admin {
//		UserManagement(controller:'um', visible: isAdmin)
//		User(controller:'user', action:'list')
//		Role(controller:'role', action:'list')
//	}
}