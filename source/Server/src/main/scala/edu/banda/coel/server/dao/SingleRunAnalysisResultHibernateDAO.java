package edu.banda.coel.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.banda.serverbase.persistence.GenericHibernate3DAO;
import com.banda.math.domain.StatsSequence;
import com.banda.math.domain.dynamics.SingleRunAnalysisResult;
import com.banda.math.domain.dynamics.SingleRunAnalysisResultType;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class SingleRunAnalysisResultHibernateDAO extends GenericHibernate3DAO<SingleRunAnalysisResult, Long> implements SingleRunAnalysisResultDAO {

	protected SingleRunAnalysisResultHibernateDAO() {
		super(SingleRunAnalysisResult.class);
	}

	@Override
	public Collection<SingleRunAnalysisResult> getListWithPropertyAndInitialState(
		SingleRunAnalysisResultType property, 
		Long multiRunId
	) {
		Collection<SingleRunAnalysisResult> singleResults = new ArrayList<SingleRunAnalysisResult>();
		final String propertyName = StringUtils.uncapitalize(property.toString());

		Collection<Object[]> idWithStatsSeqIdAndInitialStates = (Collection<Object[]>) getHibernateTemplate().
				findByNamedQueryAndNamedParam("find" + property + "AndInitialStateByMultiRunResult", "multiRunResultId", multiRunId);
		for (Object[] idWithStatsSeqIdAndInitialState : idWithStatsSeqIdAndInitialStates) {
			StatsSequence seq = new StatsSequence();
			seq.setId((Long) idWithStatsSeqIdAndInitialState[1]);
			SingleRunAnalysisResult singleRunAnalysisResult = createInstance((Long) idWithStatsSeqIdAndInitialState[0], propertyName, seq);
			singleRunAnalysisResult.setInitialState((List<Double>) idWithStatsSeqIdAndInitialState[2]);

			singleResults.add(singleRunAnalysisResult);
		}
		return singleResults;
	}

	private SingleRunAnalysisResult createInstance(Long id, String propertyName, Object value) {
		SingleRunAnalysisResult object = new SingleRunAnalysisResult();		
		setProperty(object, propertyName, value);
		setKey(object, id);
		return object;
	}
}