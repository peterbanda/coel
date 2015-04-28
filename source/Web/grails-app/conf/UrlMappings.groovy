import org.springframework.security.access.AccessDeniedException
import org.springframework.security.acls.model.NotFoundException

class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "/login/$action?"(controller: "login")
	  "/logout"(controller: "logout")

	  "/test"(uri:"/test.dispatch")

      "/"(controller: 'home', action: 'index')
	  "/download"(view:"/octave/index")

	  "/public/post/$id" {
		  controller = "post"
		  action = "showBySlug"
	   }

	  "/public/page/$id" {
		  controller = "post"
		  action = "showBySlug"
	   }

      "403"(controller: "errors", action: "error403")
      "404"(controller: "errors", action: "error404")
	  "405"(controller: "errors", action: "error405")
      "500"(controller: "errors", action: "error500")
      "500"(controller: "errors", action: "error403", exception: AccessDeniedException)
      "500"(controller: "errors", action: "error403", exception: NotFoundException)

//	  "/user/signup"(controller:'content', action:'signup')
//	  "/profile"(controller:'content', action:'profile')
//	  "/admin"(controller:'content', action:'admin')
//	  "/products"(controller:'content', action:'products')
	}
}
