<%=packageName ? "package ${packageName}\n\n" : ''%>import org.springframework.dao.DataIntegrityViolationException

class ${className}Controller {
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

	def list(Integer max) {
		params.max = Math.min(max ?: 10, 100)
//        [${propertyName}List: ${className}.list(), ${propertyName}Total: ${className}.count()]
		[${propertyName}List: ${className}.list(params), ${propertyName}Total: ${className}.count()]
	}

	def create() {
        def ${propertyName} = new ${className}(params)
//        ${propertyName}.properties = params
		return [${propertyName}: ${propertyName}]
	}

	def save() {
		def ${propertyName} = new ${className}(params)
		if (!${propertyName}.save(flush: true)) {
			render(view: "create", model: [${propertyName}: ${propertyName}])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
		redirect(action: "show", id: ${propertyName}.id)
	}

	def show(Long id) {
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "list")
			return
		}

		[${propertyName}: ${propertyName}]
	}

	def edit(Long id) {
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "list")
			return
		}

		[${propertyName}: ${propertyName}]
	}

	def update(Long id, Long version) {
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "list")
			return
		}

		if (version != null) {
			if (${propertyName}.version > version) {<% def lowerCaseName = grails.util.GrailsNameUtils.getPropertyName(className) %>
				${propertyName}.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: '${domainClass.propertyName}.label', default: '${className}')] as Object[],
						  "Another user has updated this ${className} while you were editing")
				render(view: "edit", model: [${propertyName}: ${propertyName}])
				return
			}
		}

		${propertyName}.properties = params

		if (!${propertyName}.save(flush: true)) {
			render(view: "edit", model: [${propertyName}: ${propertyName}])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
		redirect(action: "show", id: ${propertyName}.id)
	}

	def delete(Long id) {
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "list")
			return
		}

		try {
//            ${propertyName}.delete(flush: true)
//            TODO: Fix this ugly workaround
			${propertyName}.executeUpdate("delete ${className} where id = ?",[params.long('id')])			
			flash.message = message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
			redirect(action: "show", id: id)
		}
	}
}