import edu.banda.coel.web.AsyncLogger
import edu.banda.coel.web.AsyncMessageManager

import edu.banda.coel.web.GrailsBeanResolver;
import edu.banda.coel.web.GrailsExpressionHandler;
import edu.banda.coel.web.GuestAwareSuccessHandler;

import edu.banda.coel.web.SessionListener

import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy

// Place your Spring DSL code here
beans = {
	importBeans('classpath*:server.xml')
//	importBeans('classpath*:grid.xml')
    importBeans('file:grid.xml')
	importBeans('classpath*:service.xml')
	importBeans('classpath*:persistence.xml')

	acRunImageManager(AsyncMessageManager) { bean ->
		bean.parent = ref('asyncMessageManager')
	}

	appMessageManager(AsyncMessageManager) { bean ->
		bean.parent = ref('asyncMessageManager')
	}

	asyncMessageManager(AsyncMessageManager) { bean ->
		bean.initMethod = 'init'
		bean.destroyMethod = 'destroy'
	}

	asyncLogger(AsyncLogger)

	// Needed because Spring security 3.0.7 does not support @bean reference
	// could be removed after migration to a newer version
	expressionHandler(GrailsExpressionHandler) {
		beanResolver = ref('beanResolver')
		parameterNameDiscoverer = ref('parameterNameDiscoverer')
		permissionEvaluator = ref('permissionEvaluator')
		roleHierarchy = ref('roleHierarchy')
		trustResolver = ref('authenticationTrustResolver')
	}

	beanResolver(GrailsBeanResolver) {
		grailsApplication = ref('grailsApplication')
	}

    sessionListener(SessionListener)

    authenticationSuccessHandler(GuestAwareSuccessHandler) {
        def conf = SpringSecurityUtils.securityConfig
        requestCache = ref('requestCache')
        defaultTargetUrl = conf.successHandler.defaultTargetUrl // '/'
        alwaysUseDefaultTargetUrl = conf.successHandler.alwaysUseDefault // false
        targetUrlParameter = conf.successHandler.targetUrlParameter // 'spring-security-redirect'
        ajaxSuccessUrl = conf.successHandler.ajaxSuccessUrl // '/login/ajaxSuccess'
        useReferer = conf.successHandler.useReferer // false
        redirectStrategy = ref('redirectStrategy')
        chemistryCommonService = ref('chemistryCommonService')
        sessionListener = sessionListener
    }
}