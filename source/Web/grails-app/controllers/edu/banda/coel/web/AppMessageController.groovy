package edu.banda.coel.web

import edu.banda.coel.core.task.SimpleMessageEvent
import edu.banda.coel.domain.util.GeneralUtil
import edu.banda.coel.web.AsyncMessageManager
import grails.util.GrailsNameUtils

import javax.servlet.AsyncContext
import javax.servlet.AsyncEvent

import org.springframework.context.ApplicationListener

class AppMessageController extends AsyncNotificationController implements ApplicationListener<SimpleMessageEvent> { 

	static navigationScope = "none"
	def appMessageManager

	AsyncMessageManager getAsyncMessageManager() {
		appMessageManager
	}

	void onApplicationEvent(SimpleMessageEvent event) {
		if (event instanceof SimpleMessageEvent) {
			log.warn "onApplicationEvent SimpleMessageEventxxx reached";
			def message = event.message
			notifyAsEscapedString message
		}
	}
}