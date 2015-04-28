package edu.banda.coel.business;

import edu.banda.coel.domain.som.SomNode;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class SomNodeBO<TYPE> {

	private SomNode<TYPE> somNode;

	public SomNodeBO(SomNode<TYPE> somNode) {
		this.somNode = somNode;
	}
}