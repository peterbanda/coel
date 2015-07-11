package edu.banda.coel.web

import org.springframework.security.access.prepost.PostAuthorize;

import org.springframework.security.access.prepost.PreAuthorize;
import org.hibernate.transform.Transformers
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import com.banda.core.domain.um.User
import com.banda.chemistry.domain.*
import com.banda.network.domain.*
import com.banda.math.domain.evo.*
import com.banda.core.domain.task.*

class SecureAccessService {

	def createdByClasses = [ArtificialChemistry, AcCompartment, AcReactionSet, AcReactionGroup, AcSpeciesSet, AcParameterSet, AcSimulationConfig, 
							AcInteractionSeries, AcTranslationSeries, AcEvaluation, AcEvaluatedPerformance, AcRandomRatePerformance,
							AcPerturbationPerformance, ArtificialChemistrySpec, AcRateConstantTypeBound,
							Network, Topology, SpatialTopology, SpatialNeighborhood, NetworkFunction, NetworkWeightSetting, NetworkActionSeries, NetworkEvaluation,
							NetworkPerformance, SpatialNetworkPerformance,
							EvoGaSetting, EvoTask, EvoRun, ArtificialChemistrySpecBound, Task]

	def springSecurityService

	@PostAuthorize("@secureAccessService.hasAccess(returnObject)")
	def get(Class clazz, long id) {
		clazz.get(id)
	}

	@PostAuthorize("@secureAccessService.hasAccess(returnObject)")
	def getWithProjections(Class clazz, long id, properties) {
		clazz.createCriteria().get() {
			eq('id', id)
			projections {
				properties.each{ p-> property(p, p)}
			}
			resultTransformer Transformers.aliasToBean(clazz)
		}
	}

	@PreAuthorize("@secureAccessService.hasAccess(#object)")
	def save(object) {
		object.save(flush : true)
	}

	def hasAccess(object) {
		if (!object || SpringSecurityUtils.ifAllGranted('ROLE_ADMIN'))
			return true

		def userId = springSecurityService.principal?.id

		def clazz = object.getClass()
		if (clazz == User)
			return object.id == userId 

		if (createdByClasses.contains(clazz))
			return object.createdBy.id == userId

		def owner = getOwner(object)
		if (owner)
			return owner.createdBy.id == userId

		return true
	}

	private def getOwner(object) {
		if (!object) null
		switch (object) {
			case AcSpecies:
				object.parentSet
				break
			case AcParameter:
				object.parentSet
				break
			case AcReaction:
				object.reactionSet
				break
			case AcCompartmentChannel:
				object.compartment
				break
			case AcCompartmentChannelGroup:
				object.compartment
				break
			case AcInteractionVariable:
				object.parentSet
				break
			case AcInteraction:
				object.actionSeries
				break
			case AcSpeciesInteraction:
				object.action.actionSeries
				break
			case AcInteractionVariableAssignment:
				object.action.actionSeries
				break
			case AcTranslationVariable:
				object.parentSet
				break
			case AcTranslation:
				object.translationSeries
				break
			case AcTranslationItem:
				object.translation.translationSeries
				break
			case SpatialNeighbor:
				object.parent
				break
			default:
				null
				break
		}
	}
}