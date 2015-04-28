package edu.banda.coel.web

import java.util.Collection;
import java.util.HashSet;

class AcAssignmentBoundData {

	Integer type
	List<AcAssignmentData> assignments
	Double from
	Double to
}

class AcAssignmentData {
	
	Long interactionSeriesId
	Long interactionId
	Long assignmentId

	String interactionSeriesLabel
	String interactionLabel
	String assignmentLabel 
}