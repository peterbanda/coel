package edu.banda.coel.core.task;

import org.springframework.context.ApplicationEvent;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class GridTaskSpringEvent<T> extends ApplicationEvent {

	private GridTaskState<T> taskState;

	public GridTaskSpringEvent(Object source, T data) {
		super(source);
		this.taskState = new GridTaskState<T>(data);
	}

	public GridTaskSpringEvent(Object source, T data, boolean taskFinished) {
		super(source);
		this.taskState = new GridTaskState<T>(data, taskFinished); 
	}

	public GridTaskState<T> getTaskState() {
		return taskState;
	}
}
