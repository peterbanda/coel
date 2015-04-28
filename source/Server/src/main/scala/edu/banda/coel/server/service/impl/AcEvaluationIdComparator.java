package edu.banda.coel.server.service.impl;

import java.util.Comparator;

import com.banda.chemistry.domain.AcEvaluation;
import com.banda.core.util.ObjectUtil;

/**
 * @author Peter Banda
 * @since 2012
 */
@Deprecated
public class AcEvaluationIdComparator implements Comparator<AcEvaluation> {

	@Override
	public int compare(AcEvaluation as1, AcEvaluation as2) {
		return ObjectUtil.compareObjects(as1.getId(), as2.getId());
	}
}