package edu.banda.coel.domain

class Page {

    String id
    String title
    String slug
    String content
    Integer sortOrder
    Date dateCreated
    Date lastUpdated
    Boolean active = false
    Boolean includeInMenu=false

    static constraints = {
    }

    static mapping = {
        slug index: true, indexAttributes: [unique:true, dropDups:true]
    }
}
