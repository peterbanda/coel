package edu.banda.coel.web

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser

import javax.servlet.http.HttpSession

import org.springframework.security.core.context.SecurityContextHolder
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
import javax.servlet.http.HttpSessionListener
import javax.servlet.http.HttpSessionEvent

class SessionListener implements HttpSessionListener {
    def sessions = [:].asSynchronized()

    void sessionCreated (HttpSessionEvent se) {
        sessions.put(se.session.id, se.session)
    }

    void sessionDestroyed (HttpSessionEvent se) {
        sessions.remove(se.session.id)
    }

    def List<GrailsUser> getAllPrincipals() {
        def principals = []
        sessions.each { String sessionId, HttpSession session ->
            def securityContext = session[SPRING_SECURITY_CONTEXT_KEY]
            def authentication = securityContext?.authentication
//            def authentication = SecurityContextHolder.context?.authentication
            principals << authentication?.principal
        }
        principals = principals.findAll {it != null}
        principals
    }
}