package edu.banda.coel.web

import com.banda.chemistry.business.ArtificialChemistryUtil
import com.banda.chemistry.domain.*
import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl
import edu.banda.coel.domain.service.ChemistryPicGenerator
import grails.converters.JSON
import com.banda.core.domain.um.User

/**
 * Created by peter on 6/9/15.
 */
class ChemistryCommonService {

    def acUtil = ArtificialChemistryUtil.instance
    def ChemistryPicGenerator base64ChemPicGenerator = new ChemistryPicGeneratorImpl(true)
    def ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(false)

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

    def getReactionStructureImages(AcReactionSet reactionSet, boolean useBase64) {
        def images = reactionSet.reactions.collect{ reaction ->
            def structData = new SvgStructureData()
            structData.label = reaction.label
            structData.image = getStructureImage(reaction, useBase64)
            structData
        }

        images
    }

    def getSpeciesStructureImages(AcReactionSet reactionSet, boolean useBase64) {
        def refSpecies = acUtil.getReferencedSpecies(reactionSet).toList()

        def images = refSpecies.sort{ it.label }.collect{ species ->
            def structData = new SvgStructureData()
            structData.label = species.label
            if (species.structure && !species.structure.isEmpty()) {
                if (useBase64)
                    structData.image = base64ChemPicGenerator.createDNAStrandSVG(species.structure)
                else
                    structData.image = chemPicGenerator.createDNAStrandSVG(species.structure)
            }
            structData
        }

        images
    }

    private def getStructureImage(AcReaction acReaction, boolean useBase64) {
        def reactantStructures = acReaction.getSpeciesAssociations(AcSpeciesAssociationType.Reactant).collect{ assoc ->
            def structure = assoc.species.structure
            int intStoichiometry = assoc.getStoichiometricFactor().intValue()
            def structureList = []
            for (i in 1..intStoichiometry) {
                structureList.add(structure)
            }
            structureList
        }.flatten()

        def productStructures = acReaction.getSpeciesAssociations(AcSpeciesAssociationType.Product).collect{ assoc ->
            def structure = assoc.species.structure
            int intStoichiometry = assoc.getStoichiometricFactor().intValue()
            def structureList = []
            for (i in 1..intStoichiometry) {
                structureList.add(structure)
            }
            structureList
        }.flatten()

        reactantStructures = reactantStructures.findAll{ it != null && !it.isEmpty() }
        productStructures = productStructures.findAll{ it != null && !it.isEmpty() }

        if (!reactantStructures.isEmpty() || !productStructures.isEmpty()) {
            if (useBase64)
                base64ChemPicGenerator.createDNAReactionSVG(reactantStructures, productStructures, acReaction.hasReverseRateConstants())
            else
                chemPicGenerator.createDNAReactionSVG(reactantStructures, productStructures, acReaction.hasReverseRateConstants())
        } else {
            null
        }
    }

    def deleteAllGuestData(Long userId) {
        def user = User.get(userId)
        if (user.getRoles().size() != 1 || user.getRoles().first().name != "ROLE_GUEST") {
            // not a guest )
            return;
        }

        // delete compartments
        AcCompartment.findAllByCreatedBy(user).each { instance -> deleteCompartment(instance) }

        //delete reaction sets
        AcReactionSet.findAllByCreatedBy(user).each { instance ->
            AcReactionSet.withTransaction{ status ->
                instance.delete(flush: true)
            }
        }

        try {
            //delete interaction series
            def interactionSeries = AcInteractionSeries.findAllByCreatedBy(user)
            while (!interactionSeries.isEmpty()) {
                def leafInteractionSeries = findLeafInteractionSeries(interactionSeries)
                leafInteractionSeries.each { instance ->
                    if (instance.parent)
                        instance.parent.removeSubActionSeries(instance)
                    deleteInteractionSeries(instance)
                }
                interactionSeries.removeAll(leafInteractionSeries)
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        //delete translation series
        AcTranslationSeries.findAllByCreatedBy(user).each { instance -> deleteTranslationSeries(instance) }

        //delete species sets
        def speciesSets = AcSpeciesSet.findAllByCreatedBy(user)
        while (!speciesSets.isEmpty()) {
            def leafSpeciesSets = findLeafSpeciesSets(speciesSets)
            leafSpeciesSets.each { instance ->
                AcSpeciesSet.withTransaction { status ->
                    instance.delete(flush: true)
                }
            }
            speciesSets.removeAll(leafSpeciesSets)
        }

        //delete simulation configs
        AcSimulationConfig.findAllByCreatedBy(user).each { instance ->
            AcSimulationConfig.withTransaction{ status ->
                instance.delete(flush: true)
            }
        }
    }

    private def findLeafSpeciesSets(speciesSets) {
        def parentSpeciesSets = speciesSets.collect{ speciesSet -> speciesSet.parentSpeciesSet }
        def leafSpeciesSets = new HashSet(speciesSets)
        leafSpeciesSets.removeAll(parentSpeciesSets)
        leafSpeciesSets
    }

    private def findLeafInteractionSeries(interactionSeries) {
        def parentInteractionSeries = interactionSeries.collect{ singleInteractionSeries -> singleInteractionSeries.parent }
        def leafInteractionSeries = new HashSet(interactionSeries)
        leafInteractionSeries.removeAll(parentInteractionSeries)
        leafInteractionSeries
    }

    private def deleteInteractionSeries(instance) {
        instance.actions.each { action ->
            def speciesActions = new HashSet(action.speciesActions)
            speciesActions.each { speciesInteraction ->
                AcSpeciesInteraction.withTransaction { status ->
                    action.removeFromSpeciesActions(speciesInteraction)
                    speciesInteraction.delete(flush: true)
                }
            }
            def variableAssignments = new HashSet(action.variableAssignments)
            variableAssignments.each { variableAssignment ->
                AcInteractionVariableAssignment.withTransaction { status ->
                    action.removeVariableAssignment(variableAssignment)
                    variableAssignment.delete(flush: true)
                }
            }
        }

        AcInteractionSeries.withTransaction{ status ->
            instance.delete(flush: true)
        }
    }

    def deleteCompartment(instance) {
        AcCompartment.withTransaction{ status ->
            // remove sub associations
            def subAssocs = []
            subAssocs.addAll(instance.subCompartmentAssociations)
            subAssocs.each{ removeCompartmentAssociation(it)}

            // remove parent associations
            def parentAssocs = []
            parentAssocs.addAll(instance.parentCompartmentAssociations)
            parentAssocs.each{ removeCompartmentAssociation(it)}

            instance.delete(flush: true)
        }
    }

    def deleteTranslationSeries(instance) {
        AcTranslationSeries.withTransaction{ status ->
            def rangeTranslations = []
            instance.translations.each { it ->
                rangeTranslations.add(it)
            }
            rangeTranslations.each { it ->
                instance.removeTranslation(it)
                it.delete(flush : true)
            }
            instance.delete(flush: true)
        }
    }

    def removeCompartmentAssociation(compartmentAssociation) {
        def parentCompartment = compartmentAssociation.parentCompartment
        def subCompartment = compartmentAssociation.subCompartment

        AcCompartmentAssociation.withTransaction{ status ->
            parentCompartment.removeSubCompartmentAssociation(compartmentAssociation)
            compartmentAssociation.delete(flush: true)
        }
    }
}
