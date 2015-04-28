package edu.banda.coel.domain.net;

import java.util.List;

import com.banda.core.domain.Point;
import com.banda.function.domain.Function;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class FunctionNode<TYPE> extends Node<TYPE> {

	private Function<TYPE, TYPE> function;

	public FunctionNode(Function<TYPE, TYPE> function) {
		super();
		setFunction(function);
	}

	public FunctionNode(Function<TYPE, TYPE> function, Point<Integer> location) {
		super(location);
		setFunction(function);
	}

	public FunctionNode(Function<TYPE, TYPE> function, Point<Integer> location, List<? extends FunctionNode<TYPE>> neighbors) {
		super(location, neighbors);
		setFunction(function);
	}

	public Function<TYPE, TYPE> getFunction() {
		return function;
	}

	public void setFunction(Function<TYPE, TYPE> function) {
		if (getNeighbors() != null) {
			if (getNeighbors().size() != function.getArity()) {
				throw new RuntimeException("Node - " + getIdentifier() + " -  the number of node neighbors: " + getNeighbors().size() + " differs from the number of transition function's inputs: " + function.getArity());
			}
		}
		this.function = function;
	}
}