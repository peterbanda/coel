package edu.banda.coel.server.dao;

import java.util.Collection;
import java.util.List;

import com.banda.chemistry.domain.ArtificialChemistry;
import com.banda.serverbase.persistence.GenericDAO;

/**
 * @author © Peter Banda
 * @since 2012
 */
public interface ArtificialChemistryDAO extends GenericDAO<ArtificialChemistry, Long>{

	List<Object[]> getAcAndMultiRunAnalysisResultIds(Collection<Long> acIds);
}