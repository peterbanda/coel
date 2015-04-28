package edu.banda.coel.domain.evo;

import com.banda.chemistry.domain.AcInteractionVariableAssignment;
import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.core.domain.ValueBound;
import com.banda.math.domain.evo.EvoTaskType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class EvoAcInteractionSeriesTask extends EvoAcTask {

    private Set<AcSpeciesAssignmentBound> speciesAssignmentBounds = new HashSet<AcSpeciesAssignmentBound>();
    private Set<AcInteractionVariableAssignmentBound> variableAssignmentBounds = new HashSet<AcInteractionVariableAssignmentBound>();

    public EvoAcInteractionSeriesTask() {
        super();
        setTaskType(EvoTaskType.AcInteractionSeries);
    }

    public Set<AcSpeciesAssignmentBound> getSpeciesAssignmentBounds() {
        return speciesAssignmentBounds;
    }

    public void setSpeciesAssignmentBounds(Set<AcSpeciesAssignmentBound> speciesAssignmentBounds) {
        this.speciesAssignmentBounds = speciesAssignmentBounds;
    }

    public Set<AcInteractionVariableAssignmentBound> getVariableAssignmentBounds() {
        return variableAssignmentBounds;
    }

    public void setVariableAssignmentBounds(Set<AcInteractionVariableAssignmentBound> variableAssignmentBounds) {
        this.variableAssignmentBounds = variableAssignmentBounds;
    }

    public void addSpeciesAssignmentBound(AcSpeciesAssignmentBound bound) {
        speciesAssignmentBounds.add(bound);
    }

    public void addVariableAssignmentBound(AcInteractionVariableAssignmentBound bound) {
        variableAssignmentBounds.add(bound);
    }

    // util methods - TODO: move elsewhere

    public Map<AcInteractionVariableAssignment, ValueBound<Double>> getVariableAssignmentBoundMap() {
        Map<AcInteractionVariableAssignment, ValueBound<Double>> boundMap = new HashMap<AcInteractionVariableAssignment, ValueBound<Double>>();
        if (variableAssignmentBounds != null)
            for (AcInteractionVariableAssignmentBound variableAssignmentBound : variableAssignmentBounds)
                for (AcInteractionVariableAssignment assignment : variableAssignmentBound.getAssignments())
                    boundMap.put(assignment, variableAssignmentBound.getBound());

        return boundMap;
    }

    public Map<AcSpeciesInteraction, ValueBound<Double>> getSpeciesAssignmentBoundMap() {
        Map<AcSpeciesInteraction, ValueBound<Double>> boundMap = new HashMap<AcSpeciesInteraction, ValueBound<Double>>();
        if (speciesAssignmentBounds != null)
            for (AcSpeciesAssignmentBound speciesAssignmentBound : speciesAssignmentBounds)
                for (AcSpeciesInteraction assignment : speciesAssignmentBound.getAssignments())
                    boundMap.put(assignment, speciesAssignmentBound.getBound());

        return boundMap;
    }
}