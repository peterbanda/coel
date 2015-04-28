package edu.banda.coel.web

import groovy.util.logging.Log

import com.banda.core.domain.um.User

import com.banda.chemistry.domain.AcSpeciesSet
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

abstract class BaseController {

	static defaultSimConfig = 1010l
	static navigationScope = "none"

	def springSecurityService

	//	def beforeInterceptor = {
	//		request.layout = layoutName
	//	}

	User getCurrentUserOrError() {
		def user = currentUser
		if (!user) {
			Log.error "No user logged, but expected."
			response.sendError(404)
			return null
		}
		return user
	}

	def isAdmin = {
		SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')
	}

	User getCurrentUser() {
		if (!springSecurityService.isLoggedIn()) {
			return null
		}

		return User.get(springSecurityService.principal.id)
	}

	// species set stuff... TODO: relocate
	
	def getThisAndDerivedSpeciesSets(speciesSet) {
		getDerivedSpeciesSetsRecursively(speciesSet) + speciesSet
	}

	def getDerivedSpeciesSetsRecursively(speciesSet) {
		def derivedSpeciesSets = AcSpeciesSet.findAllByParentSpeciesSet(speciesSet, [sort:'id', order:'desc'])
		if (!derivedSpeciesSets.isEmpty())
			(derivedSpeciesSets.collect{ getDerivedSpeciesSetsRecursively(it) }.findAll{!it.isEmpty()} + derivedSpeciesSets).flatten()
		else []
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
}