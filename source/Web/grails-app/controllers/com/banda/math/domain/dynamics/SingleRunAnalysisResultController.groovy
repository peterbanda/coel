package com.banda.math.domain.dynamics

import edu.banda.coel.web.BaseDomainController

class SingleRunAnalysisResultController extends BaseDomainController {

	def index() {
		redirect(action: "list", params: params)
	}
}