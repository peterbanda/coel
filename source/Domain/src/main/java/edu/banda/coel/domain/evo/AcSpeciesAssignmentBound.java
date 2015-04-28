package edu.banda.coel.domain.evo;

import java.util.Collection;
import java.util.HashSet;

import com.banda.chemistry.domain.AcSpeciesInteraction;
import com.banda.core.domain.TechnicalDomainObject;
import com.banda.core.domain.ValueBound;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class AcSpeciesAssignmentBound extends TechnicalDomainObject {

	private Collection<AcSpeciesInteraction> assignments = new HashSet<AcSpeciesInteraction>();
	private ValueBound<Double> bound = new ValueBound<Double>();

	public Collection<AcSpeciesInteraction> getAssignments() {
		return assignments;
	}

	public void setAssignments(Collection<AcSpeciesInteraction> assignments) {
		this.assignments = assignments;
	}

	public void addAssignment(AcSpeciesInteraction assignment) {
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