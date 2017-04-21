package com.banda.core.domain.um

import edu.banda.coel.web.BaseDomainController
import grails.converters.JSON
import org.apache.commons.io.IOUtils
import org.json.simple.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class UserController extends BaseDomainController {

    private reCaptchaSecret = getGrailsApplication().config.recaptcha.site.secret
    private reCaptchaVerificationUrl = "https://www.google.com/recaptcha/api/siteverify"

    def showLoggedUser = {
		def user = getCurrentUser()
		render(view: "show", model: [instance: user])
	}

	def editLoggedUser = {
		def user = getCurrentUser()
		render(view: "edit", model: [instance: user])
	}

	def changePasswordLoggedUser = {
		def user = getCurrentUser()
		render(view: "edit", model: [instance: user, changePassword : true])
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def create() {
		[instance: new User(params)]
	}

	def register() {
		[instance: new User(params)]
	}

	def getLoggedUserEmailMD5 = {
		def user = getCurrentUser()
		render user.email.encodeAsMD5()
	}

	def setSidebar = {
		session["sidebar"] = "true"
		render "success"
	}

	def unsetSidebar = {
		session["sidebar"] = "false"
		render "success"
	}

	def save() {
		def userInstance = new User(params)
		userInstance.createTime = new Date()
		if (params.password)
			userInstance.password = springSecurityService.encodePassword(params.password)

		if (params.username && User.countByUsername(params.username) > 0) {
			userInstance.errors.rejectValue(
				"username",
				message(code: 'default.not.unique.message', args: ['username', 'User', params.username]))
		}

		if (params.email && User.countByEmail(params.email) > 0) {
			userInstance.errors.rejectValue(
				"email",
				message(code: 'default.not.unique.message', args: ['email', 'User', params.email]))
		}

		if (userInstance.hasErrors() || !userInstance.save(flush: true)) {
			render(view: "create", model: [instance: userInstance])
			return
		}
		
		flash.message = message(code: 'default.created.message', args: [doClazzMessageLabel, userInstance.id])
		redirect(action: "show", id: userInstance.id)
	}

	def registerSave() {
		def userInstance = new User(params)
		userInstance.createTime = new Date()

        if (!verifyReCaptcha(params["g-recaptcha-response"])) {
            userInstance.errors.rejectValue("", "reCAPTCHA not filled or incorrect. It seems you're a robot.")
        }

		if (params.password) {
			if (params.password.length() < 8) {
				userInstance.errors.rejectValue(
					"password",
					"Password must be at least 8 characters long.")
			}
			userInstance.password = springSecurityService.encodePassword(params.password)
		}

		if (params.username && User.countByUsername(params.username) > 0) {
			userInstance.errors.rejectValue(
				"username",
				message(code: 'default.not.unique.message', args: ['username', 'User', params.username]))
		}

		if (params.email && User.countByEmail(params.email) > 0) {
			userInstance.errors.rejectValue(
				"email",
				message(code: 'default.not.unique.message', args: ['email', 'User', params.email]))
		}

        userInstance.accountEnabled = false
        userInstance.roles = []

		if (userInstance.hasErrors() || !userInstance.save(flush: true)) {
			render(view: "register", model: [instance: userInstance])
			return
		}

		log.info message(code: 'user.registered.message', args: [userInstance.id, userInstance.username])
		
		sendMail {
		    from "Coel Admin <coelsim@cecs.pdx.edu>"
			to "banda@pdx.edu"
			subject "User registered"
			body message(code: 'user.registered.message', args: [userInstance.id, userInstance.username])
		}
		render(view: "registerSuccess", model: [instance: userInstance])
	}

    private verifyReCaptcha(reCaptchaResponse) {
        URL url = new URL(reCaptchaVerificationUrl)
        def postData = "secret=" + reCaptchaSecret + "&response=" + reCaptchaResponse
        byte[] postDataBytes = postData.getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        def response = IOUtils.toString(conn.getInputStream())
        (boolean) JSON.parse(response).get("success")
    }

	def activate(Long id) {
		def userInstance = getSafe(id)
		if (!userInstance) {
			handleObjectNotFound(id)
			return
		}

		userInstance.accountEnabled = true
		def extUserRole = Role.findByName("ROLE_EXT_USER")
		userInstance.addRole(extUserRole)

		if (!userInstance.save(flush: true)) {
			render(view: "show", model: [instance: userInstance])
			return
		}

		sendMail {
		    from "Coel Admin <coelsim@cecs.pdx.edu>"
			to userInstance.email
			subject "COEL account activated"
//			body render(template:"accountActivated", model: ["user": userInstance])
//			body message(code: 'user.accountactivated.message', args: [userInstance.firstName])
			html "<p>" + message(code: 'user.accountactivated.message', args: [userInstance.firstName]) + "</p>"
		}

		flash.message = "User " + userInstance.id + " activated."
		redirect(action: "show", id: userInstance.id)
	}

	def update(Long id, Long version) {
		def userInstance = getSafe(id)
		if (!userInstance) {
			handleObjectNotFound(id)
			return
		}
	
		if (version != null)
			if (userInstance.version > version) {
				setOlcFailureMessage(userInstance)
				render(view: "edit", model: [instance: userInstance])
				return
			}

		userInstance.properties = params
		userInstance.changeTime = new Date()

		if (userInstance.email != params.email)
			if (User.countByEmail(params.email) > 0) {
				userInstance.errors.rejectValue(
					"email",
					message(code: 'default.not.unique.message', args: ['email', 'User', params.email]))
			}

		if (userInstance.hasErrors() || !updateSafe(userInstance)) {
			render(view: "edit", model: [instance: userInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [doClazzMessageLabel, userInstance.id])
		if (userInstance.id == getCurrentUser().id)
			redirect(action: "showLoggedUser")
		else
			redirect(action: "show", id: userInstance.id)
	}

	def changePassword(Long id, String password) {
		def userInstance = getSafe(id)
		if (!userInstance) {
			handleObjectNotFound(id)
			return
		}

		if (password) {
			userInstance.password = springSecurityService.encodePassword(password)
			if (!userInstance.save(flush: true)) {
				flash.message = 'An attempt to change the password for user ' + id + ' failed.'
			} else {
				flash.message = 'The password changed for user ' + id + '.'
			}
		} else {
			flash.message = 'An attempt to change the password for user ' + id + ' failed.'
		}
		if (userInstance.id == getCurrentUser().id)
			redirect(action: "showLoggedUser")
		else
			redirect(action: "show", id: userInstance.id)
	}
}