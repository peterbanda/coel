package edu.banda.coel.domain.evo;

import com.banda.chemistry.domain.AcInteractionVariableAssignment;
import com.banda.core.domain.TechnicalDomainObject;
import com.banda.core.domain.ValueBound;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class AcInteractionVariableAssignmentBound extends TechnicalDomainObject {

    private Collection<AcInteractionVariableAssignment> assignments = new HashSet<AcInteractionVariableAssignment>();
    private ValueBound<Double> bound = new ValueBound<Double>();

    public Collection<AcInteractionVariableAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Collection<AcInteractionVariableAssignment> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(AcInteractionVariableAssignment assignment) {
        assignments.add(assignment);
    }

    public ValueBound<Double> getBound() {
        return bound;
    }

    public void setBound(ValueBound<Double> bound) {
        this.bound = bound;
    }

    public Double getFrom() {
        return bound.getFrom();
    }

    public Double getTo() {
        return bound.getTo();
    }

    public void setFrom(Double from) {
        bound.setFrom(from);
    }

    public void setTo(Double to) {
        bound.setTo(to);
    }
}