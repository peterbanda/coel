package edu.banda.coel.business.evo;

import java.io.Serializable;

import com.banda.math.business.evo.EvoTaskBO;
import com.banda.math.domain.evo.EvoGaSetting;
import com.banda.math.domain.evo.EvoTask;
import com.banda.math.task.EvoRunTask;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask;
import edu.banda.coel.domain.evo.EvoAcRateConstantTask;
import edu.banda.coel.domain.evo.EvoAcSpecTask;
import edu.banda.coel.domain.evo.EvoNetworkTask;

public class EvoTaskBOFactorySwitch implements Serializable {

	private final EvoTaskBOFactory<EvoAcRateConstantTask> evoAcRateConstantTaskBOFactory;
	private final EvoTaskBOFactory<EvoAcInteractionSeriesTask> evoAcInteractionSeriesTaskBOFactory;
	private final EvoTaskBOFactory<EvoAcSpecTask> evoAcSpecTaskBOFactory;
	private final EvoTaskBOFactory<EvoNetworkTask> evoNetworkTaskBOFactory;

	public EvoTaskBOFactorySwitch(
		EvoTaskBOFactory<EvoAcRateConstantTask> evoAcRateConstantTaskBOFactory,
		EvoTaskBOFactory<EvoAcInteractionSeriesTask> evoAcInteractionSeriesTaskBOFactory,
		EvoTaskBOFactory<EvoAcSpecTask> evoAcSpecTaskBOFactory,
		EvoTaskBOFactory<EvoNetworkTask> evoNetworkTaskBOFactory
	) {
		this.evoAcRateConstantTaskBOFactory = evoAcRateConstantTaskBOFactory;
		this.evoAcInteractionSeriesTaskBOFactory = evoAcInteractionSeriesTaskBOFactory;
		this.evoAcSpecTaskBOFactory = evoAcSpecTaskBOFactory;
		this.evoNetworkTaskBOFactory = evoNetworkTaskBOFactory;
	}

	public EvoTaskBO<?, ?, ?> createInstance(EvoRunTask Task) {
		EvoTask evoTask = Task.getEvoTask();
		EvoGaSetting gaSetting = evoTask.getGaSetting();
		EvoTaskBO<?, ?, ?> evoTaskBO = null;
		if (evoTask instanceof EvoAcRateConstantTask)
			evoTaskBO = evoAcRateConstantTaskBOFactory.createInstance((EvoAcRateConstantTask) evoTask, gaSetting);
		else if (evoTask instanceof EvoAcInteractionSeriesTask)
			evoTaskBO = evoAcInteractionSeriesTaskBOFactory.createInstance((EvoAcInteractionSeriesTask) evoTask, gaSetting);
		else if (evoTask instanceof EvoAcSpecTask)
			evoTaskBO = evoAcSpecTaskBOFactory.createInstance((EvoAcSpecTask) evoTask, gaSetting);
		else if (evoTask instanceof EvoNetworkTask)
			evoTaskBO = evoNetworkTaskBOFactory.createInstance((EvoNetworkTask<?>) evoTask, gaSetting);
		else
			throw new CoelRuntimeException("Evo task type '" + evoTask.getClass().getName() + "' not recognized. Please register in EvoTaskBOFactorySwitch class.");
		return evoTaskBO;
	}
}