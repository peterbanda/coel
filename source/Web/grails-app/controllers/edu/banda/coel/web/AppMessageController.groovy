package edu.banda.coel.web

import edu.banda.coel.core.task.SimpleMessageEvent
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