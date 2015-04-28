package com.banda.core.domain.um
constraints = {
	username(size:1..20, blank: false, nullable: false, unique: true)
	password(size:8..100, blank: false, nullable: false)
	email(email: true, size:1..40, blank: false, nullable: false, unique: true)
	firstName(size:1..20, blank: false, nullable: false)
	lastName(size:1..30, blank: false, nullable: false)
	affiliation(size:1..80, blank: false, nullable: false)
	intendedUse(size:1..1000, blank: false, nullable: false)
}