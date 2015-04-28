package com.banda.chemistry.domain
constraints = {
	name(size:1..100, blank: false)
	odeSolverType(nullable: false)
	timeStep(nullable: false)
}