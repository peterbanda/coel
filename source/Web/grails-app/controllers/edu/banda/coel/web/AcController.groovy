package edu.banda.coel.web

import com.banda.chemistry.domain.AcCompartment
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.domain.AcTranslationSeries

class AcController extends BaseController {

	def index = {
		def compartmentsEmpty = true
		def acActionSeriesEmpty = true
		def acTranslationSeriesEmpty = true
		if (isAdmin()) {
			compartmentsEmpty = AcCompartment.count() == 0
			acActionSeriesEmpty = AcInteractionSeries.count() == 0
			acTranslationSeriesEmpty = AcTranslationSeries.count() == 0
		} else {
			def user = getCurrentUserOrError()
			compartmentsEmpty = AcCompartment.countByCreatedBy(user) == 0
			acActionSeriesEmpty = AcInteractionSeries.countByCreatedBy(user) == 0
			acTranslationSeriesEmpty = AcTranslationSeries.countByCreatedBy(user) == 0
		}
		[compartmentsEmpty : compartmentsEmpty, acActionSeriesEmpty : acActionSeriesEmpty, acTranslationSeriesEmpty : acTranslationSeriesEmpty]
	}
}
