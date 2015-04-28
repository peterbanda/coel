import edu.banda.coel.web.AsyncLogger
import edu.banda.coel.web.AsyncMessageManager
import com.banda.chemistry.domain.ArtificialChemistry

import edu.banda.coel.web.GrailsBeanResolver;
import edu.banda.coel.web.GrailsExpressionHandler;

// Place your Spring DSL code here
beans = {
	importBeans('classpath*:server.xml')
	importBeans('classpath*:grid.xml')
//    importBeans('file:grid.xml')
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

//	secureDAO(SecureDAO)
//		clazz = ArtificialChemistry.class
//	}
}