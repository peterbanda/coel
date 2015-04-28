package edu.banda.coel.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.banda.chemistry.domain.AcMultiRunAnalysisResult;
import com.banda.serverbase.persistence.GenericHibernate3DAO;
import com.banda.math.domain.Stats;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class AcMultiRunAnalysisResultHibernateDAO extends GenericHibernate3DAO<AcMultiRunAnalysisResult, Long> implements AcMultiRunAnalysisResultDAO {

	protected AcMultiRunAnalysisResultHibernateDAO() {
		super(AcMultiRunAnalysisResult.class);
	}

	@Override
	public List<Stats> getStatsForPropertyOnly(SingleRunAnalysisResultType property, Collection<Long> multiRunIds) {
		if (multiRunIds.isEmpty()) {
			return new ArrayList<Stats>();
		}
		return getHibernateTemplate().
				findByNamedQueryAndNamedParam("find" + property + "Stats", "multiRunResultIds", multiRunIds);
	}

	@Override
	public List<?> getForPropertyOnly(SingleRunAnalysisResultType property, Collection<Long> multiRunIds) {
		if (multiRunIds.isEmpty()) {
			return new ArrayList<Object>();
		}
		return getHibernateTemplate().
				findByNamedQueryAndNamedParam("find" + property, "multiRunResultIds", multiRunIds);
	}
}