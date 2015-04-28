package edu.banda.coel.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Peter Banda
 */
public class AsyncMessageManager {

	private final Queue<AsyncContext> contextQueue = new ConcurrentLinkedQueue<AsyncContext>();
	private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

	private Thread notifierThread;

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Initialize; called by Spring as the init-method.
	 */
	public void init() {
		notifierThread = new Thread() {
			@Override
			public void run() {
				boolean done = false;
				while (!done) {
					try {
						processNextMessage();
					}
					catch (InterruptedException e) {
						done = true;
						log.error(e.getMessage());
					}
				}
			}
		};
		notifierThread.start();
	}

	protected void processNextMessage() throws InterruptedException {
		String message = messageQueue.take();
		for (AsyncContext ac : contextQueue) {
			try {
				PrintWriter acWriter = findResponse(ac).getWriter();
				acWriter.println(message);
				acWriter.flush();
			}
			catch (IOException e) {
				log.error(e.getMessage());
				contextQueue.remove(ac);
			}
		}
	}

	/**
	 * Grails wraps the response to implement performance optimizations, but
	 * flush() calls are ignored. So we need to find the real response and write
	 * directly to it.
	 *
	 * @param ac the async context
	 * @return the real response
	 */
	protected ServletResponse findResponse(AsyncContext ac) {
		ServletResponse response = ac.getResponse();
		while (response instanceof ServletResponseWrapper) {
			response = ((ServletResponseWrapper)response).getResponse();
		}
		return response;
	}

	/**
	 * Destroy; called by Spring as the destroy-method.
	 */
	public void destroy() {
		contextQueue.clear();
		notifierThread.interrupt();
	}

	/**
	 * Send a new message.
	 * @param message the message
	 * @throws IOException
	 */
	public void notify(String message) throws IOException {
		try {
			messageQueue.put(message);
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Register a new context.
	 * @param asyncContext the context
	 */
	public void register(AsyncContext asyncContext) {
		contextQueue.add(asyncContext);
	}

	/**
	 * Unregister a context.
	 * @param asyncContext the context
	 */
	public void remove(AsyncContext asyncContext) {
		contextQueue.remove(asyncContext);
	}
}