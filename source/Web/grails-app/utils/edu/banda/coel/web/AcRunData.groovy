package edu.banda.coel.web

class AcRunData {

	List species
	List speciesRunHistories
	List<Double> timeSteps
	Double runTime

	def addSpeciesRunHistorySequences(Collection additionalSequences) {
		additionalSequences.eachWithIndex{ additionalSequence, index ->
//			println "Additional Sequence '${additionalSequence}'"
			if (speciesRunHistories[index] == null) {
				speciesRunHistories[index] = []
			}
			speciesRunHistories[index].addAll(additionalSequence)
		}
	}
}