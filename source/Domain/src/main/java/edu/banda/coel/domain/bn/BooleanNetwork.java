package edu.banda.coel.domain.bn;

import java.util.Collection;

import edu.banda.coel.domain.net.FunctionNode;
import edu.banda.coel.domain.net.SpatialNetwork;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BooleanNetwork extends SpatialNetwork<Boolean, FunctionNode<Boolean>> {

	private Integer connectivityPerNode;                // optional

	protected BooleanNetwork(Collection<FunctionNode<Boolean>> nodes) {
		super(nodes);
	}

	// TODO : change to protected
	public BooleanNetwork(Collection<FunctionNode<Boolean>> nodes, Integer connectivityPerNode) {
		this(nodes);
		this.connectivityPerNode = connectivityPerNode;
	}

	// TODO: change to protected
	public BooleanNetwork(Collection<FunctionNode<Boolean>> nodes, Integer connectivityPerNode, boolean torusFlag) {
		this(nodes, connectivityPerNode);
		setTorusFlag(torusFlag);
	}

	public Integer getConnectivityPerNode() {
		return connectivityPerNode;
	}
}