import grails.plugins.springsecurity.Secured

@Secured(['permitAll'])
class ErrorsController {

	static navigationScope = "none"

	def error403 = {
		println '403'
		render(view: "/errors/error403")
	}

	def error404 = {
		println '404'
		render(view: "/errors/error404")
	}

	def error405 = {
		println '405'
		render(view: "/errors/error405")
	}

	def error500 = {
		println '505'
		render view: '/error'
	}
}