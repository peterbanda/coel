package edu.banda.coel.web

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsHttpSession
import org.springframework.web.context.request.RequestContextHolder

class WebUtilService {
	
	void withSession (Closure closure) {
		try {
			GrailsWebRequest request = RequestContextHolder.currentRequestAttributes()
			GrailsHttpSession session = request.session
			closure.call(session)
		}
		catch (IllegalStateException ise) {
			log.warn ("No WebRequest available!")
		}
	}
}	  