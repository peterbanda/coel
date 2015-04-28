package edu.banda.coel.web;

import java.io.IOException;

import javax.servlet.AsyncEvent;

public class AsyncCleanupListener extends AsyncListenerAdapter {

	private AsyncMessageManager asyncMessageManager;

	public AsyncCleanupListener(AsyncMessageManager asyncMessageManager) {
		this.asyncMessageManager = asyncMessageManager;
	}

	public void onComplete(AsyncEvent event) {
		asyncMessageManager.remove(event.getAsyncContext());
	}

	public void onError(AsyncEvent event) {
		asyncMessageManager.remove(event.getAsyncContext());
	}

	public void onTimeout(AsyncEvent event) throws IOException {
		asyncMessageManager.remove(event.getAsyncContext());
	}
}