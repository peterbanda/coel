package edu.banda.coel.web;

import org.codehaus.groovy.grails.plugins.springsecurity.AjaxAwareAuthenticationSuccessHandler;

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.StringUtils;
import edu.banda.coel.web.SessionListener;

import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

class GuestAwareSuccessHandler extends AjaxAwareAuthenticationSuccessHandler {

    private RequestCache requestCache;
    private ChemistryCommonService chemistryCommonService;
    private SessionListener sessionListener;

    @Override
    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response) {

        GrailsUser principal = (GrailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = (Long) principal.getId();
        boolean isGuest = SpringSecurityUtils.ifAllGranted("ROLE_GUEST");
        List<GrailsUser> loggedUsers = sessionListener.getAllPrincipals();

        if (isGuest) {
            for (GrailsUser loggedUser : loggedUsers)
                if (loggedUser.getUsername().equals("guest")) {
                    SecurityContextHolder.clearContext();
                    return "/login/authGuestInUsedFail";
                }
            // clean-up
            chemistryCommonService.deleteAllGuestData(userId);
        }
        return super.determineTargetUrl(request, response);
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {
         super.setRequestCache(requestCache);
         this.requestCache = requestCache;
    }

    public void setChemistryCommonService(ChemistryCommonService chemistryCommonService) {
        this.chemistryCommonService = chemistryCommonService;
    }

    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }
}