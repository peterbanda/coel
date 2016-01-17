import com.banda.core.domain.um.User

import org.hibernate.transform.Transformers
import org.hibernate.criterion.Projections
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import edu.banda.coel.web.SessionListener

class BootStrap {

	def grailsApplication
	def SessionListener sessionListener
//	def authenticationManager
//	def authenticationProcessingFilter
//	def concurrentSessionControlStrategy
//	def securityContextPersistenceFilter

     def init = { servletContext ->
		 servletContext.addListener(sessionListener)
//		 authenticationProcessingFilter.sessionAuthenticationStrategy = concurrentSessionControlStrategy
//		 SpringSecurityUtils.clientRegisterFilter('concurrencyFilter', SecurityFilterPosition.CONCURRENT_SESSION_FILTER)

		 def doClasses = grailsApplication.getArtefacts("Domain")*.clazz
		 doClasses.each { doClazz -> registerDynamicMethods(doClazz)}
     }

	 private def registerDynamicMethods(clazz) {

		 clazz.metaClass.static.listWithProjections = { properties ->
			 clazz.createCriteria().list([sort:'id', order:'desc']) {
				 projections {
					 properties.each{ p-> property(p, p)}
				 }
//				 order("id", "desc")
				 resultTransformer Transformers.aliasToBean(clazz)
			 }
		 }
 
		 clazz.metaClass.static.listWithParamsAndProjections = { params ->
			 clazz.createCriteria().list(params) {
				 if (params?.createdBy)
				 	eq('createdBy', params.createdBy)

				 if (params?.projections)
				 	projections {
						 params.projections.each{ p-> property(p, p)}
					 }
				 resultTransformer Transformers.aliasToBean(clazz)
			 }
		 }

		 clazz.metaClass.static.nextId = { id ->
			 clazz.createCriteria().get() { // [sort:'id', order:'asc']
//				 order('id', "asc")
				 gt('id', id)
//				 maxResults(1)
				 projections {
					 min('id')
				 }
			 }
		 }

		 clazz.metaClass.static.previousId = { id ->
			 clazz.createCriteria().get() {
//				 order('id', "desc")
				 lt('id', id)
//				 maxResults(1)
				 projections {
					 max('id')
				 }
			 }
		 }
	 }
 
     def destroy = {
     }
} 