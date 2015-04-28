package com.banda.chemistry.domain
constraints = {
	name(size:1..100, blank: false)
	skinCompartment(nullable: false)
	simulationConfig(nullable: false)
}