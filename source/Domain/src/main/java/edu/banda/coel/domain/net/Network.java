package edu.banda.coel.domain.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class Network<TYPE, NODE_TYPE extends Node<TYPE>> {

	private Collection<NODE_TYPE> nodes = new ArrayList<NODE_TYPE>();

	public Network() {
		// empty constructor
	}

	public Network(Collection<NODE_TYPE> nodes) {
		this();
		this.nodes = nodes;
	}

	public Collection<NODE_TYPE> getNodes() {
		return nodes;
	}

	protected void setNodes(Collection<NODE_TYPE> nodes) {
		this.nodes = nodes;
	}

	public void addNode(NODE_TYPE node) {
		nodes.add(node);
	}

	public void removeNode(NODE_TYPE node) {
		nodes.remove(node);
	}

	public boolean hasNodes() {
		return nodes != null && !nodes.isEmpty();
	}

	public int getNumberOfNodes() {
		return hasNodes() ? nodes.size() : 0;
	}

	public Collection<TYPE> getNodesStates() {
		Collection<TYPE> nodesStates = new ArrayList<TYPE>();
		for (Node<TYPE> node : nodes) {
			nodesStates.add(node.getState());
		}
		return nodesStates;
	}

	public Collection<TYPE> getPreviousNodesStates() {
		List<TYPE> prevNodesStates = new ArrayList<TYPE>();
		for (Node<TYPE> node : nodes) {
			prevNodesStates.add(node.getPreviousState());
		}
		return prevNodesStates;
	}

	public void setNodesStates(List<TYPE> nodesStates) {
		Iterator<TYPE> nodesStateIterator = nodesStates.iterator();
		for (Node<TYPE> node : nodes) {
			node.setState(nodesStateIterator.next());
		}
	}
}