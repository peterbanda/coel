package edu.banda.coel.core.task;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class GridTaskState<T> {

	private T data;
	private boolean taskFinished = false;

	public GridTaskState(T data) {
		this.data = data;
	}

	public GridTaskState(T data, boolean taskFinished) {
		this(data);
		this.taskFinished = taskFinished;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isTaskFinished() {
		return taskFinished;
	}

	public void setTaskFinished(boolean taskFinished) {
		this.taskFinished = taskFinished;
	}
}
