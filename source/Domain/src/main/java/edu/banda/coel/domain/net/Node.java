package edu.banda.coel.domain.net;

import java.util.ArrayList;
import java.util.List;

import com.banda.core.domain.Point;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class Node<TYPE> {

	private Integer identifier;                    // optional
	private TYPE state;
	private TYPE previousState;                    // optional
	private List<Node<TYPE>> neighbors = new ArrayList<Node<TYPE>>();
	private Point<Integer> location;               // used when metric space is needed

	public Node() {
		// Nothing to do
	}

	public Node(Point<Integer> location) {
		this.location = location;
	}

	public Node(Point<Integer> location, List<? extends Node<TYPE>> neighbors) {
		this(location);
		this.neighbors = (List<Node<TYPE>>) neighbors;
	}

	protected void setState(TYPE state) {
		this.state = state;
	}

	public TYPE getState() {
		return state;
	}

	public void addNeighbor(Node<TYPE> node) {
		neighbors.add(node);
	}

	public void removeNeighbor(Node<TYPE> node) {
		neighbors.remove(node);
	}

	public List<? extends Node<TYPE>> getNeighbors() {
		return neighbors;
	}

	public int getNeighborsNumber() {
		return neighbors != null ? neighbors.size() : 0;
	}

    public List<TYPE> getNeighborstates() {
    	List<TYPE> neighborstates = new ArrayList<TYPE>();
    	for (Node<TYPE> neighbor : getNeighbors()) {
    		neighborstates.add(neighbor.getState());
    	}
    	return neighborstates;
    }

	public boolean hasLocation() {
		return location != null; 
	}

	public Point<Integer> getLocation() {
		return location;
	}

	public void setLocation(Point<Integer> location) {
		this.location = location;
	}

	public void set1dLocation(Integer coordinate) {
		this.location = new Point<Integer>(
				new Integer[] {coordinate});
	}

	public void set2dLocation(Integer coordinate1, Integer coordinate2) {
		this.location = new Point<Integer>(
				new Integer[] {coordinate1, coordinate2});
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public TYPE getPreviousState() {
		return previousState;
	}

	public void setPreviousState(TYPE previousState) {
		this.previousState = previousState;
	}

	public Boolean getChangedNodeStateFlag() {
		if (getState().equals(getPreviousState())) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;				
	}
}