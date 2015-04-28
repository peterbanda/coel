package edu.banda.coel.task.chemistry;

import com.banda.chemistry.domain.AcRateConstantType;
import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.core.domain.ValueBound;
import com.banda.core.util.ObjectUtil;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleRateConstantTypeBoundHolder;

import java.util.*;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRandomRatePerformanceEvaluateTask extends AcPerformanceEvaluateTask implements AcMultipleRateConstantTypeBoundHolder {

    private Integer randomRateGenerationNum;
    private Collection<AcRateConstantTypeBound> rateConstantTypeBounds = new HashSet<AcRateConstantTypeBound>();

    @Override
    public Collection<AcRateConstantTypeBound> getRateConstantTypeBounds() {
        return rateConstantTypeBounds;
    }

    @Override
    public void setRateConstantTypeBounds(Collection<AcRateConstantTypeBound> rateConstantTypeBounds) {
        this.rateConstantTypeBounds = rateConstantTypeBounds;
    }

    public void addRateConstantTypeBound(AcRateConstantTypeBound rateConstantTypeBound) {
        rateConstantTypeBounds.add(rateConstantTypeBound);
    }

    @Override
    public Collection<Long> getRateConstantTypeBoundIds() {
        Collection<Long> ids = new ArrayList<Long>();
        for (AcRateConstantTypeBound acRateConstantTypeBound : rateConstantTypeBounds) {
            ids.add(acRateConstantTypeBound.getId());
        }
        return ids;
    }

    @Override
    public boolean areRateConstantTypeBoundDefined() {
        return rateConstantTypeBounds != null;
    }

    @Override
    public boolean areRateConstantTypeBoundComplete() {
        return ObjectUtil.getFirst(rateConstantTypeBounds).getRateConstantType() != null;
    }

    public void setRateConstantTypeBoundIds(Collection<Long> rateConstantTypeBoundIds) {
        if (rateConstantTypeBoundIds == null) {
            return;
        }
        rateConstantTypeBounds.clear();
        for (Long rateConstantTypeBoundId : rateConstantTypeBoundIds) {
            AcRateConstantTypeBound acRateConstantTypeBound = new AcRateConstantTypeBound();
            acRateConstantTypeBound.setId(rateConstantTypeBoundId);
            addRateConstantTypeBound(acRateConstantTypeBound);
        }
    }

    public Map<AcRateConstantType, ValueBound<Double>> getReactionRateConstantTypeBoundMap() {
        Map<AcRateConstantType, ValueBound<Double>> reactionRateConstantBoundMap = new HashMap<AcRateConstantType, ValueBound<Double>>();
        if (rateConstantTypeBounds != null) {
            for (AcRateConstantTypeBound acRateConstantTypeBound : rateConstantTypeBounds) {
                reactionRateConstantBoundMap.put(acRateConstantTypeBound.getRateConstantType(), acRateConstantTypeBound.getBound());
            }
        }
        return reactionRateConstantBoundMap;
    }

    public Integer getRandomRateGenerationNum() {
        return randomRateGenerationNum;
    }

    public void setRandomRateGenerationNum(Integer randomRateGenerationNum) {
        this.randomRateGenerationNum = randomRateGenerationNum;
    }
}