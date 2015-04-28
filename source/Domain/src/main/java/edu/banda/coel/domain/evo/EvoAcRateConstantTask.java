package edu.banda.coel.domain.evo;

import com.banda.chemistry.domain.AcRateConstantType;
import com.banda.chemistry.domain.AcRateConstantTypeBound;
import com.banda.chemistry.domain.AcReaction;
import com.banda.chemistry.domain.AcReactionGroup;
import com.banda.core.domain.ValueBound;
import com.banda.math.domain.evo.EvoTaskType;

import java.util.*;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class EvoAcRateConstantTask extends EvoAcTask {

    private Collection<AcRateConstantTypeBound> rateConstantTypeBounds = new HashSet<AcRateConstantTypeBound>();
    private Collection<AcReactionGroup> fixedRateReactionGroups = new HashSet<AcReactionGroup>();
    private Collection<AcReaction> fixedRateReactions = new HashSet<AcReaction>();

    public EvoAcRateConstantTask() {
        super();
        setTaskType(EvoTaskType.AcRateConstant);
    }

    public Collection<AcReactionGroup> getFixedRateReactionGroups() {
        return fixedRateReactionGroups;
    }

    public void setFixedRateReactionGroups(Collection<AcReactionGroup> fixedRateReactionGroups) {
        this.fixedRateReactionGroups = fixedRateReactionGroups;
    }

    public void addFixedRateReactionGroup(AcReactionGroup fixedRateReactionGroup) {
        fixedRateReactionGroups.add(fixedRateReactionGroup);
    }

    public Collection<AcReaction> getFixedRateReactions() {
        return fixedRateReactions;
    }

    public void setFixedRateReactions(Collection<AcReaction> fixedRateReactions) {
        this.fixedRateReactions = fixedRateReactions;
    }

    public void addFixedRateReaction(AcReaction fixedRateReaction) {
        fixedRateReactions.add(fixedRateReaction);
    }

    public Collection<AcRateConstantTypeBound> getRateConstantTypeBounds() {
        return rateConstantTypeBounds;
    }

    public void setRateConstantTypeBounds(Collection<AcRateConstantTypeBound> rateConstantTypeBounds) {
        this.rateConstantTypeBounds = rateConstantTypeBounds;
    }

    public void addRateConstantTypeBound(AcRateConstantTypeBound rateConstantTypeBound) {
        rateConstantTypeBounds.add(rateConstantTypeBound);
    }

    public void initRateConstantTypeBounds() {
        rateConstantTypeBounds = new ArrayList<AcRateConstantTypeBound>();
    }

    public Map<AcRateConstantType, ValueBound<Double>> getReactionRateConstantTypeBoundMap() {
        Map<AcRateConstantType, ValueBound<Double>> reactionRateConstantBoundMap = new HashMap<AcRateConstantType, ValueBound<Double>>();
        if (rateConstantTypeBounds != null) {
            for (AcRateConstantTypeBound rateConstantTypeBound : rateConstantTypeBounds) {
                reactionRateConstantBoundMap.put(rateConstantTypeBound.getRateConstantType(), rateConstantTypeBound.getBound());
            }
        }
        return reactionRateConstantBoundMap;
    }
}