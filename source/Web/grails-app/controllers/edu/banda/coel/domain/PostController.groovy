package edu.banda.coel.domain

import edu.banda.coel.web.BaseDomainController

class PostController extends BaseDomainController {

	PostController() {
		super(Post.class)
	}

	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		params.projections = ['id','dateCreated','title','slug']
		super.list(max)
	}

	def listView() {
		params.projections = ['id','dateCreated','title','slug','content']
        params.sort = 'id'
        params.order = 'desc'

        def posts = Post.listWithParamsAndProjections(params)
		[list : posts]
	}

	def create() {
		[instance: new Post(params)]
	}

	def save() {
		def postInstance = new Post(params)

		postInstance.author = currentUserOrError.id

		if (!postInstance.save(flush: true)) {
			render(view: "create", model: [instance: postInstance])
			return
		}
	
		flash.message = message(code: 'default.created.message', args: [message(code: 'post.label', default: 'Post'), postInstance.id])
		redirect(action: "show", id: postInstance.id)
	}

	def showBySlug(String id) {
		Post postInstance = Post.findBySlug(id)
		render(view: 'show', model: [instance : postInstance])
	}

	def update(Long id, Long version) {
		def postInstance = getSafe(id)
		if (!postInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), id])
			redirect(action: "list")
			return
		}
	
		if (version != null) {
			if (postInstance.version > version) {
				postInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'post.label', default: 'Post')] as Object[],
						  "Another user has updated this Post while you were editing")
					render(view: "edit", model: [postInstance: postInstance])
					return
			}
		}

		postInstance.properties = params
	
		if (!postInstance.save(flush: true)) {
			render(view: "edit", model: [instance: postInstance])
			return
		}
	
		flash.message = message(code: 'default.updated.message', args: [message(code: 'post.label', default: 'Post'), postInstance.id])
		redirect(action: "show", id: postInstance.id)
	}
}