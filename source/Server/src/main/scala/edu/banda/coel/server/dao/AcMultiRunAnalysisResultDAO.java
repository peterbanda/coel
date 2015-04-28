package edu.banda.coel.server.dao;

import java.util.Collection;
import java.util.List;

import com.banda.chemistry.domain.AcMultiRunAnalysisResult;
import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.Stats;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

/**
 * @author © Peter Banda
 * @since 2013
 */
public interface AcMultiRunAnalysisResultDAO extends GenericDAO<AcMultiRunAnalysisResult, Long>{

	List<Stats> getStatsForPropertyOnly(SingleRunAnalysisResultType property, Collection<Long> multiRunIds);

	List<?> getForPropertyOnly(SingleRunAnalysisResultType property, Collection<Long> multiRunIds);
}