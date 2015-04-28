package edu.banda.coel.domain.net;

import edu.banda.coel.CoelRuntimeException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class LayeredNetwork<TYPE, NODE_TYPE extends Node<TYPE>> extends Network<TYPE, NODE_TYPE> {

    private Set<NODE_TYPE>[] layers;

    public LayeredNetwork() {
        super();
    }

    public LayeredNetwork(Collection<NODE_TYPE> nodes, int layersNum) {
        super(nodes);
        if (layersNum < 1) {
            throw new CoelRuntimeException("The number of layer for network ('" + layersNum + "') is not legal.");
        }
        this.layers = new HashSet[layersNum];
    }

    public LayeredNetwork(Collection<NODE_TYPE> nodes, Set<NODE_TYPE>[] layers) {
        super(nodes);
        this.layers = layers;
    }

    public Set<NODE_TYPE>[] getLayers() {
        return layers;
    }

    protected void setLayers(Set<NODE_TYPE>[] layers) {
        this.layers = layers;
    }

    public boolean hasLayers() {
        return layers != null && layers.length > 0;
    }

    public void addNode(int layerIndex, NODE_TYPE node) {
        if (hasLayers() && layers.length > layerIndex) {
            layers[layerIndex].add(node);
        } else {
            throw new CoelRuntimeException("Attemp to add a node to non-existing layer with index " + layerIndex + ".");
        }
    }
}