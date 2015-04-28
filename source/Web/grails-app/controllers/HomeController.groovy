import edu.banda.coel.domain.Post

class HomeController {

	static navigationScope = "none"

	def index = {
		def posts = Post.listWithProjections(['id','title','dateCreated','author'])
		render(view: '/index', model: [posts : posts])
	}
}