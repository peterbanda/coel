package edu.banda.coel.business.evo;

import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.domain.evo.EvoGaSetting;
import com.banda.math.domain.evo.EvoTask;

public interface EvoTaskBOFactory<C extends EvoTask> {

	EvoTaskBO<?, ?, ?> createInstance(C evoTask, EvoGaSetting gaSetting);
}