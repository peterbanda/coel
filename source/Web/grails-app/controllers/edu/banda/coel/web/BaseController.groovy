package edu.banda.coel.web

import com.banda.core.domain.um.User
import groovy.util.logging.Log
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
}