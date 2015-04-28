package edu.banda.coel.web

import grails.util.GrailsNameUtils

import javax.servlet.AsyncContext
import javax.servlet.AsyncEvent

import org.springframework.context.ApplicationListener

import edu.banda.coel.core.task.GridTaskSpringEvent
import edu.banda.coel.core.task.GridTaskState;
import edu.banda.coel.core.task.SimpleMessageEvent
import edu.banda.coel.core.util.ImageUtils
import edu.banda.coel.web.AsyncListenerAdapter
import edu.banda.coel.web.AsyncLogger
import edu.banda.coel.web.AsyncMessageManager
import edu.banda.coel.domain.util.GeneralUtil
import org.springframework.context.ApplicationListener

import edu.banda.coel.task.chemistry.AcRunEventData

import edu.banda.coel.core.task.GridTaskSpringEvent
import edu.banda.coel.web.*
import grails.converters.JSON

@Deprecated
class AcRunImageController extends AsyncNotificationController implements ApplicationListener<GridTaskSpringEvent<AcRunEventData>> { 

	static navigationScope = "none"
	def acRunImageManager

	AsyncMessageManager getAsyncMessageManager() {
		acRunImageManager
	}

	private void notify(GridTaskState<AcRunEventData> taskState) {
		def callBackFunName = getCallbackFunctionName()
		def acRunEventData = taskState.data
		def acSpeciesRunHistorySequences = acRunEventData.speciesRunHistorySequences
		
		JSON.use("deep") {
			def acRunData = session.acRunData
			if (acRunData == null) {
				acRunData = new AcRunData()
				acRunData.speciesRunHistories = []
				session.acRunData = acRunData
			}
			acRunData.addSpeciesRunHistorySequences(acSpeciesRunHistorySequences)
			session.evaluatedActions = acRunEventData.evaluatedActions

			def myImage = ImageUtils.generateSimpleAcChart(550, 250, acRunData.speciesRunHistories)
			def imageString = ImageUtils.toBase64StringWithoutNewlines(myImage, 'gif')

			def acSimulationData = new AcSimulationData()
			acSimulationData.lastStep = acRunData.steps
			acSimulationData.simulationImage = imageString
			acSimulationData.runFinished = taskState.taskFinished

			def acSimulationDataJSON = acSimulationData as JSON

			notifyAsJSON acSimulationDataJSON
		}
	}

	void onApplicationEvent(GridTaskSpringEvent<AcRunEventData> event) {
		if (event instanceof GridTaskSpringEvent) {
			def taskState = event.taskState
			notify taskState
		}
	}
}