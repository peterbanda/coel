package edu.banda.coel.web;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

/**
 * Adapter class for AsyncListener to subclass when you don't need to implement all methods.
 *
 * @author Burt Beckwith
 */
public abstract class AsyncListenerAdapter implements AsyncListener {

	public void onComplete(AsyncEvent event) throws IOException {
		// override as needed
	}

	public void onTimeout(AsyncEvent event) throws IOException {
		// override as needed
	}

	public void onError(AsyncEvent event) throws IOException {
		// override as needed
	}

	public void onStartAsync(AsyncEvent event) throws IOException {
		// override as needed
	}
}
