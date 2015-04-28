package edu.banda.coel.web;

import grails.util.GrailsUtil;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Burt Beckwith
 */
public class AsyncLogger implements AsyncListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	public void onStartAsync(AsyncEvent event) throws IOException {
		if (!log.isDebugEnabled()) {
			return;
		}
		log.debug("onStartAsync for request " + System.identityHashCode(event.getSuppliedRequest()));
	}

	public void onComplete(AsyncEvent event) {
		if (!log.isDebugEnabled()) {
			return;
		}
		log.debug("onComplete for request " + System.identityHashCode(event.getSuppliedRequest()));
	}

	public void onError(AsyncEvent event) {
		if (event.getThrowable() == null) {
			// ??
		}
		else {
			GrailsUtil.deepSanitize(event.getThrowable());
			log.error(event.getThrowable().getMessage(), event.getThrowable());
		}
	}

	public void onTimeout(AsyncEvent event) throws IOException {
		log.debug("onTimeout for request " + System.identityHashCode(event.getSuppliedRequest()));
	}
}
