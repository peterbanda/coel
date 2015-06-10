package edu.banda.coel.web

import edu.banda.coel.domain.util.GeneralUtil
import grails.util.GrailsNameUtils

import javax.servlet.AsyncContext

abstract class AsyncNotificationController {

	protected static final String JUNK = '<!-- Comet is a programming technique that enables web servers to send data to the client without having any need for the client to request it. -->\n'
	protected static final long TIMEOUT = 10 * 60 * 1000 // 10 minutes
	protected final String logicalName = GrailsNameUtils.getLogicalName(this.getClass(), "Controller")
	protected final String callbackFunctionName = "callback" + logicalName

	// dependency injected
	AsyncLogger asyncLogger

	def beforeInterceptor = {
		response.setHeader 'Cache-Control', 'private'
		response.setHeader 'Pragma', 'no-cache'
	}

	def listen() {
		response.characterEncoding = 'UTF-8'
		response.contentType = 'text/html'

		// for Safari, Chrome, IE and Opera
		PrintWriter writer = response.writer
		10.times { writer.write(JUNK) }
		writer.flush()

		AsyncContext ac = startAsync()
		ac.timeout = TIMEOUT
		ac.addListener asyncLogger

		ac.addListener new AsyncCleanupListener(asyncMessageManager)

		asyncMessageManager.register ac
	}

	def notifyAsJSON(data) {
		asyncMessageManager.notify "<script>parent.${callbackFunctionName}(${data});</script>\n"
	}

	def notifyAsString(data) {
		asyncMessageManager.notify "<script>parent.${callbackFunctionName}('${data}');</script>\n"
	}

	def notifyAsEscapedString(data) {
		notifyAsString(GeneralUtil.escapeHTML(data))
	}

	protected abstract AsyncMessageManager getAsyncMessageManager();

	protected String getCallbackFunctionName() {
		callbackFunctionName
	}
}