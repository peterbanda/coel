package edu.banda.coel.domain

class Comment implements Serializable {

    String title
    String name
    String email
    String website
    String content
    Date dateCreated
    String md5Hash

    public String getMd5Hash() {
        return this.email?email.trim().encodeAsMD5() : ''
    }

	static mapping = {
		table "blog_comment"
	}
}
