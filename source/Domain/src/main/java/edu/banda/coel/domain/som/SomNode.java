package edu.banda.coel.domain.som;

import java.util.List;

import edu.banda.coel.domain.net.Node;
import edu.banda.coel.domain.util.GeneralUtil;

/**
 * @author Peter Banda
 * @since 2011
 */
public class SomNode<TYPE> extends Node<TYPE[]> {

	public SomNode() {
		super(); 
	}
	public SomNode(TYPE[] weigths) {
		this();
		setWeights(weigths); 
	}

	/**
	 * Set the weights - the SOM node's state.
	 * 
	 * @param weights
	 */
	public void setWeights(TYPE[] weights) {
		setState(weights);
	}

	/**
	 * Get the weights - the SOM node's state.
	 * 
	 * @return weights
	 */
	public TYPE[] getWeights() {
		return getState();
	}

	public TYPE getWeight(int index) {
		GeneralUtil.checkElement(index, getWeights());
		return getWeights()[index];
	}

	public void setWeight(int index, TYPE value) {
		getWeights()[index] = value;
	}

	@SuppressWarnings("unchecked")
	public List<SomNode<TYPE>> getTypedNeighbors() {
		return (List<SomNode<TYPE>>) getNeighbors();
	}
}