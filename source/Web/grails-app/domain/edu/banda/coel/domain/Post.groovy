package edu.banda.coel.domain

class Post {

    String title
    String slug
    String content
    List<String> tags
    Date dateCreated
    Date lastUpdated
    Long author
    Boolean active=false
	
//	  Integer commentsCount = 0
//    List<Comment> comments

//    static embedded = ['comments']
//    static hasMany = [comments: Comment, tags: String]
	
	static hasMany = [tags: String]

	static constraints = {
		content(maxSize:5000)
	}

//    static constraints = {
//        commentsCount nullable: true
//    }

    static mapping = {
		table "blog_post"
        slug index: true, indexAttributes: [unique:true, dropDups:true]
    }
}