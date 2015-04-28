package edu.banda.coel.server.dao;

import java.util.Collection;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.dynamics.SingleRunAnalysisResult;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

/**
 * @author © Peter Banda
 * @since 2013
 */
public interface SingleRunAnalysisResultDAO extends GenericDAO<SingleRunAnalysisResult, Long>{

	Collection<SingleRunAnalysisResult> getListWithPropertyAndInitialState(
			SingleRunAnalysisResultType property, Long multiRunId);
}