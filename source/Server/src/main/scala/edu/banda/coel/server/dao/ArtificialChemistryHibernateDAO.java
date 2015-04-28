package edu.banda.coel.server.dao;

import java.util.Collection;
import java.util.List;

import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.serverbase.persistence.GenericHibernate3DAO;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class ArtificialChemistryHibernateDAO extends GenericHibernate3DAO<ArtificialChemistry, Long> implements ArtificialChemistryDAO {

	protected ArtificialChemistryHibernateDAO() {
		super(ArtificialChemistry.class);
	}

	@Override
	public List<Object[]> getAcAndMultiRunAnalysisResultIds(Collection<Long> acIds) {
		return getHibernateTemplate().
				findByNamedQueryAndNamedParam("findMultiRunAnalysisResultIds", "acIds", acIds);
	}
}