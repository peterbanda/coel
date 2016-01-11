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
import org.springframework.util.StringUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

class GuestAwareSuccessHandler extends AjaxAwareAuthenticationSuccessHandler {

    private RequestCache requestCache;
    private ChemistryCommonService chemistryCommonService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        Long userId = (Long) ((GrailsUser) authentication.getPrincipal()).getId();
        boolean isGuest = SpringSecurityUtils.ifAllGranted("ROLE_GUEST");
        if (isGuest) {
            // clean-up
            chemistryCommonService.deleteAllGuestData(userId);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    public void setRequestCache(RequestCache requestCache) {
         super.setRequestCache(requestCache);
         this.requestCache = requestCache;
    }

    public void setChemistryCommonService(ChemistryCommonService chemistryCommonService) {
        this.chemistryCommonService = chemistryCommonService;
    }
}