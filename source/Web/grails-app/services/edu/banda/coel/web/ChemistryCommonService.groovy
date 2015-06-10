package edu.banda.coel.web

import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcInteractionVariableAssignment
import com.banda.chemistry.domain.AcSpeciesSet

/**
 * Created by peter on 6/9/15.
 */
class ChemistryCommonService {

    def getThisAndDerivedSpeciesSets(speciesSet) {
        getDerivedSpeciesSetsRecursively(speciesSet) + speciesSet
    }

    def getThisAndParentSpeciesSets(speciesSet) {
        def speciesSets = []
        speciesSets.add(speciesSet)

        def speciesSetAux = speciesSet
        while (speciesSetAux.parentSpeciesSet) {
            speciesSetAux = speciesSetAux.parentSpeciesSet
            speciesSets.add(speciesSetAux)
        }
        speciesSets
    }

    private def getDerivedSpeciesSetsRecursively(speciesSet) {
        def derivedSpeciesSets = AcSpeciesSet.findAllByParentSpeciesSet(speciesSet, [sort:'id', order:'desc'])
        if (!derivedSpeciesSets.isEmpty())
            (derivedSpeciesSets.collect{ getDerivedSpeciesSetsRecursively(it) }.findAll{!it.isEmpty()} + derivedSpeciesSets).flatten()
        else []
    }

    def saveActionSeries(AcInteractionSeries acInteractionSeriesInstanceClone) {
        def interactionVariableAssignmentMap = [ : ]
        acInteractionSeriesInstanceClone.actions.each{action ->
            interactionVariableAssignmentMap.put(action, action.variableAssignments)
            action.setVariableAssignments(new HashSet<AcInteractionVariableAssignment>())
        }
        if (!acInteractionSeriesInstanceClone.save(flush: true)) {
            return false
        }
        // now add variable assignments and save again
        interactionVariableAssignmentMap.entrySet().each { actionInteractionVariableAssignments ->
            def action = actionInteractionVariableAssignments.getKey()
            def interactionVariableAssignments = actionInteractionVariableAssignments.getValue()
            action.addVariableAssignments(interactionVariableAssignments)
            if (!action.save(flush: true)) {
//                render(view: "edit", model: [instance: acInteractionSeriesInstance])
                return false
            }
        }
        return true
    }

    def saveActionSeriesRecursively(acInteractionSeriesInstance, actionSeriesCloneMap) {
        def actionSeriesClone = actionSeriesCloneMap.get(acInteractionSeriesInstance)
        actionSeriesClone.removeAllSubActionSeries()

        acInteractionSeriesInstance.subActionSeries.each{
            saveActionSeriesRecursively(it, actionSeriesCloneMap)
        }
        acInteractionSeriesInstance.subActionSeries.each{
            def subActionSeriesClone = actionSeriesCloneMap.get(it)
            actionSeriesClone.addSubActionSeries(subActionSeriesClone)
        }
        saveActionSeries(actionSeriesClone)
    }
}
