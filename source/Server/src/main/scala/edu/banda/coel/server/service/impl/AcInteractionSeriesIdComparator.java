package edu.banda.coel.server.service.impl;

import java.util.Comparator;

import com.banda.chemistry.domain.AcInteractionSeries;
import com.banda.core.util.ObjectUtil;

public class AcInteractionSeriesIdComparator implements Comparator<AcInteractionSeries> {

	@Override
	public int compare(AcInteractionSeries as1, AcInteractionSeries as2) {
		return ObjectUtil.compareObjects(as1.getId(), as2.getId());
	}
}
