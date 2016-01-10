import grails.plugins.springsecurity.SecurityConfigType;

// import org.springframework.core.io.support.PathMatchingResourcePatternResolver
// import org.codehaus.groovy.grails.compiler.support.GrailsResourceLoaderHolder
// import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
// import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA
// import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


//def ctx = AH.application.mainContext
//def resourceHolder = GrailsResourceLoaderHolder.resourceLoader
//def resolver = new PathMatchingResourcePatternResolver(ctx)
//def resources = resolver.findPathMatchingResources("classpath:*server.properties")
//grails.config.locations = [ "${resources[0].filename}" ]
//grails.config.locations = [ "classpath:server.properties" ]
grails.config.locations = [ "classpath:server.properties" ]

// grails.tomcat.nio = true
grails.project.groupId = edu.banda.coel // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'
grails.views.javascript.library="jquery"

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder=false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable fo AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = ['edu.banda.coel.web']
grails.app.context="/"
grails.validateable.classes = [edu.banda.coel.task.chemistry.AcRunTask]

/*
// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://coel-sim.org"
    }
    development {
        grails.serverURL = "http://coel-sim.org"
    }
    test {
        grails.serverURL = "http://coel-sim.org"
    }
}
*/

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	rootLogger = "info,stdout"

	info   "StackTrace"

    info   'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails.web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
           'net.sf.ehcache.hibernate',
		   'org.hibernate',
		   'edu.banda.coel'

    warn   'org.mortbay.log'
}

grails {
	mail {
	  host = System.getenv("COEL_EMAIL_HOST")
	  port = System.getenv("COEL_EMAIL_PORT")
	  username = System.getenv("COEL_EMAIL_USERNAME")
	  password = System.getenv("COEL_EMAIL_PASSWORD")
	  props = ["mail.smtp.auth":"true",
			   "mail.smtp.socketFactory.port":System.getenv("COEL_EMAIL_PORT"),
			   "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
			   "mail.smtp.socketFactory.fallback":"false"]
 
	}
}

// plugin.platformCore.site.url='localhost:8080'
grails.plugin.platformCore.site.name=COEL
grails.plugins.platformCore.site.name=COEL
plugin.platformCore.site.name=COEL

plugin.platformUi.themes.Bootstrap.ui.set = "Bootstrap"

plugin.platformUi.ui.Bootstrap.actions.cssClass = 'form-actions'
plugin.platformUi.ui.Bootstrap.button.cssClass = 'btn'
plugin.platformUi.ui.Bootstrap.tab.cssClass = 'tab-pane'
plugin.platformUi.ui.Bootstrap.tabs.cssClass = 'nav nav-tabs'
plugin.platformUi.ui.Bootstrap.field.cssClass = 'input'
plugin.platformUi.ui.Bootstrap.input.cssClass = 'input-xlarge'
plugin.platformUi.ui.Bootstrap.invalid.cssClass = 'invalid'
plugin.platformUi.ui.Bootstrap.table.cssClass = 'table table-striped'
plugin.platformUi.ui.Bootstrap.tr.cssClass = ''
plugin.platformUi.ui.Bootstrap.trOdd.cssClass = ''
plugin.platformUi.ui.Bootstrap.trEven.cssClass = ''
plugin.platformUi.ui.Bootstrap.carousel.cssClass = 'carousel slide'
plugin.platformUi.ui.Bootstrap.slide.cssClass = 'item'
plugin.platformUi.ui.Bootstrap.form.cssClass = 'form-horizontal'
plugin.platformUi.ui.Bootstrap.primaryNavigation.cssClass = 'nav'
plugin.platformUi.ui.Bootstrap.secondaryNavigation.cssClass = 'nav nav-pills'
plugin.platformUi.ui.Bootstrap.navigation.cssClass = 'nav'

// Spring Security
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.banda.core.domain.um.User'
grails.plugins.springsecurity.userLookup.authoritiesPropertyName = 'roles'
grails.plugins.springsecurity.userLookup.enabledPropertyName = 'accountEnabled'
grails.plugins.springsecurity.userLookup.accountExpiredPropertyName = 'accountExpired'
grails.plugins.springsecurity.userLookup.accountLockedPropertyName = 'accountLocked'
grails.plugins.springsecurity.userLookup.passwordExpiredPropertyName = 'credentialsExpired'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.banda.core.domain.um.UserRole'
grails.plugins.springsecurity.authority.className = 'com.banda.core.domain.um.Role'
grails.plugins.springsecurity.authority.nameField = 'name'

//grails.plugin.springsecurity.secureChannel.useHeaderCheckChannelSecurity = true
//grails.plugin.springsecurity.portMapper.httpPort = 8080
//grails.plugin.springsecurity.portMapper.httpsPort = 443
//grails.plugin.springsecurity.auth.forceHttps = true
//grails.plugin.springsecurity.secureChannel.secureHeaderName = 'X-Forwarded-Proto'
//grails.plugin.springsecurity.secureChannel.secureHeaderValue = 'http'
//grails.plugin.springsecurity.secureChannel.insecureHeaderName = 'X-Forwarded-Proto'
//grails.plugin.springsecurity.secureChannel.insecureHeaderValue = 'https'

grails.plugins.springsecurity.auth.loginFormUrl = '/login/auth'
//grails.plugins.springsecurity.successHandler.defaultTargetUrl= '/login/authsuccess'
//grails.plugins.springsecurity.successHandler.alwaysUseDefault = true
grails.plugins.springsecurity.failureHandler.defaultFailureUrl = '/login/authfail'
grails.plugins.springsecurity.errors.login.expired = 'Your account has expired. Contact your administrator.'
grails.plugins.springsecurity.errors.login.passwordExpired = 'Your password has expired. Contact your administrator.'
grails.plugins.springsecurity.errors.login.disabled = 'Your account has not been activated yet or has been disabled. Wait for the registration to complete or contact your administrator.'
grails.plugins.springsecurity.errors.login.locked = 'Your account has been locked. Contact your administrator.'
grails.plugins.springsecurity.errors.login.fail = 'The username or password you entered is incorrect. Try again.'

grails.plugins.springsecurity.adh.errorPage = null

//grails.plugins.springsecurity.securityConfigType = SecurityConfigType.Annotation
grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugins.springsecurity.interceptUrlMap = [
	'/':                                      ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/content/**':                            ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/download':                              ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/login/**':                              ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/logout/**':                             ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/download/**':                           ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/errors/**':                             ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/favicon.ico':                           ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/user/register':                         ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/user/registerSave':                     ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/post/listView':                         ['IS_AUTHENTICATED_ANONYMOUSLY'],
	'/user/showLoggedUser':                   ['ROLE_EXT_USER', 'ROLE_USER'],
	'/user/editLoggedUser':                   ['ROLE_EXT_USER', 'ROLE_USER'],
	'/user/changePasswordLoggedUser':         ['ROLE_EXT_USER', 'ROLE_USER'],
	'/user/update':                           ['ROLE_EXT_USER', 'ROLE_USER'],
	'/user/changePassword':                   ['ROLE_EXT_USER', 'ROLE_USER'],
	'/user/getLoggedUserEmailMD5':            ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/user/**':                               ['ROLE_ADMIN'],
	'/role/**':                               ['ROLE_ADMIN'],
	'/um/**':                                 ['ROLE_ADMIN'],
	'/jasper/**':                             ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/ac/**':                                 ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/artificialChemistry/generateACs':       ['ROLE_USER'],
	'/artificialChemistry/**':                ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acCompartment/**':                      ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acReactionSet/**':                      ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acReaction/**':                         ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acReactionGroup/**':                    ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acSpeciesSet/**':                       ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acSpecies/**':                          ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acParameterSet/**':                     ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acParameter/**':                        ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acInteractionSeries/**':                ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acInteraction/**':                      ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acSpeciesInteraction/**':               ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acInteractionVariableAssignment/**':    ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acInteractionVariable/**':              ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acTranslationSeries/**':                ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acTranslation/**':                      ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acTranslationItem/**':                  ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
    '/acTranslationVariable/**':              ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acSimulationConfig/**':                 ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/acRun/**':                              ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/chemPic/**':                            ['ROLE_GUEST', 'ROLE_EXT_USER', 'ROLE_USER'],
	'/**':                                    ['ROLE_USER'],
]