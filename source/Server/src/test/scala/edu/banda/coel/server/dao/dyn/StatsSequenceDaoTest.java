package edu.banda.coel.server.dao.dyn;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.banda.serverbase.persistence.GenericDAO;
import com.banda.math.domain.StatsSequence;

import edu.banda.coel.server.dao.CoelBasicDaoTest;

/**
 * @author Peter Banda
 * @since 2011
 */
public class StatsSequenceDaoTest extends CoelBasicDaoTest<StatsSequence, Long> {

	private static final Long EXISTING_STATS_SEQUENCE_ID = 1000l;

	@Autowired
	GenericDAO<StatsSequence, Long> statsSequenceDAO;

	@Override
	public void setUpTestData() {
		setUp(statsSequenceDAO, StatsSequence.class, EXISTING_STATS_SEQUENCE_ID);
	}

	@Test
	public void testGetMultiple() {
		final Collection<StatsSequence> seqs = statsSequenceDAO.get(Arrays.asList(new Long[] {1000l, 1001l, 1002l}));
		assertNotNull(seqs);
		for (StatsSequence seq : seqs) {
			assertNotEmpty(seq.getStats());
		}
	}
}