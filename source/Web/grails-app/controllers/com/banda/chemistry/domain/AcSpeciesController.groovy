package com.banda.chemistry.domain

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl
import edu.banda.coel.domain.service.ChemistryPicGenerator
import edu.banda.coel.core.svg.SVGUtil;
import edu.banda.coel.web.BaseDomainController

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException

import com.banda.chemistry.domain.AcSpecies
import com.banda.chemistry.domain.AcSpeciesSet
import com.banda.core.util.ParseUtil

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.lang.StringUtils

import grails.converters.JSON

class AcSpeciesController extends BaseDomainController {

	def ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(true);

	def saveAjax() {
		AcSpeciesSet speciesSet = AcSpeciesSet.get(params.parentSet.id)

		def acSpeciesLabels = StringUtils.split(params.labels, ', ')
		def labels = []
		def acSpeciesInstances = []
		acSpeciesLabels.each{ label ->
            def trimmedLabel = label.trim()
            if (trimmedLabel.matches("[a-zA-Z_\$][\\w\$]*")) {
                def acSpeciesInstance = new AcSpecies()
                acSpeciesInstance.label = trimmedLabel
                speciesSet.addVariable(acSpeciesInstance)

                if (!acSpeciesInstance.save(flush: true)) {
                    render(view: "create", model: [instance: acSpeciesInstance])
                    return
                }
                labels.add(acSpeciesInstance.label)
                acSpeciesInstances.add(acSpeciesInstance)
            }
		}

		def size = labels.size()
		def text = null
		if (size == 0)
            text = message(code: 'species.notcreated.message')
        else if (size == 1)
			text = message(code: 'default.created.message', args: [doClazzMessageLabel, labels.get(0)])
		else
			text = message(code: 'default.multicreated.message', args: [doClazzMessageLabel, labels.join(", "), size])
		render ([message: text, acSpeciesInstances : acSpeciesInstances] as JSON)
	}

	def show(Long id) {
		def result = super.show(id)
		def structureImage = getStructureImage(result.instance)
		result << [structureImage : structureImage]
	}

	def edit(Long id) {
		def result = super.edit(id)
		def structureImage = getStructureImage(result.instance)
		result << [structureImage : structureImage]
	}

	def update(Long id, Long version) {
		def acSpeciesInstance = getSafe(id)
		if (!acSpeciesInstance) {
			handleObjectNotFound(id)
			return
		}

		if (version != null)
			if (acSpeciesInstance.version > version) {
				setOlcFailureMessage(acSpeciesInstance)
				render(view: "edit", model: [instance: acSpeciesInstance])
				return
			}

		acSpeciesInstance.properties = params

		try {
			if (acSpeciesInstance.structure)
				chemPicGenerator.createDNAStrandSVG(acSpeciesInstance.structure)
		} catch (e) {
			acSpeciesInstance.errors.rejectValue("structure","Structure: " + e.message)
		}
		
		if (acSpeciesInstance.hasErrors() || !acSpeciesInstance.save(flush: true)) {
			render(view: "edit", model: [instance: acSpeciesInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'acSpecies.label', default: 'AC Species'), acSpeciesInstance.id])
		redirect(controller: "acSpeciesSet", action: "show", id: acSpeciesInstance.parentSet.id)
	}

	protected def deleteInstance(instance) {
		def speciesSet = instance.parentSet
		AcSpecies.withTransaction{ status ->
			speciesSet.removeVariable(instance)
			instance.delete(flush: true)
		}
	}
 
	protected def notFoundRedirect(id) {
		redirect(controller: "acSpeciesSet", action: "list")
	}
 
	protected def deleteSuccessfulRedirect(instance, owner) {
		redirect(controller: "acSpeciesSet", action: "show", id: owner.id)
	}
 
	protected def deleteMultiSuccessfulRedirect(instancesWithOwners) {
		redirect(controller: "acSpeciesSet", action: "show", id: instancesWithOwners.first().value.id)
	}
 
	protected def getOwner(instance) {
		instance.parentSet
	}

	private def getStructureImage(acSpecies) {
		if (acSpecies.structure && !acSpecies.structure.isEmpty()) {
			chemPicGenerator.createDNAStrandSVG(acSpecies.structure)
		} else {
			null
		}
	}
}