package edu.banda.coel.task.network;

/**
 * @author Peter Banda
 * @since 2013
 */
public class SpatialNetworkPerformanceEvaluateTask<T> extends NetworkPerformanceEvaluateTask<T> {

    private Integer sizeFrom;
    private Integer sizeTo;

    public SpatialNetworkPerformanceEvaluateTask() {
        super();
    }

    public Integer getSizeFrom() {
        return sizeFrom;
    }

    public void setSizeFrom(Integer sizeFrom) {
        this.sizeFrom = sizeFrom;
    }

    public Integer getSizeTo() {
        return sizeTo;
    }

    public void setSizeTo(Integer sizeTo) {
        this.sizeTo = sizeTo;
    }
}