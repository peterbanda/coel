package edu.banda.coel.core.task;

import org.springframework.context.ApplicationEvent;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class SimpleMessageEvent extends ApplicationEvent {

	private final String message;

	public SimpleMessageEvent(String message) {
		super(new Object());
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
