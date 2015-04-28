package edu.banda.coel.server.service.impl;

import com.banda.serverbase.grid.ComputationalGrid;
import net.sf.beanlib.hibernate.HibernateBeanReplicator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.banda.serverbase.persistence.GenericDAO;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.domain.rc.RcAcSetting;
import edu.banda.coel.domain.rc.RcMachineTemplate;
import edu.banda.coel.domain.service.ReservoirComputingService;
import edu.banda.coel.task.RcTaskParts.RcMachineTemplateHolder;

/**
 * Title: ReservoirComputingServiceImpl
 *
 * @see edu.banda.coel.domain.service.ReservoirComputingService
 * @author Peter Banda
 * @since 2012
 */
class ReservoirComputingServiceImpl implements ReservoirComputingService {

	/**
	 * Data Access Objects used by the service methods in this class to access
	 * persistent layer.
	 */
	private GenericDAO<RcMachineTemplate, Long> rcMachineTemplateDAO;

	/**
	 * Grid wrapper provides executions of predefined jobs on the computational grid.  
	 */
	private ComputationalGrid grid;

	private HibernateBeanReplicator hibernateReplicator;

    protected final Log log = LogFactory.getLog(getClass());

	//////////////
	// SERVICES //
	//////////////

	@Override
	@Transactional(readOnly = false)
	public void runLearning(RcAcSetting rcSetting) {
		log.info("Reservoir computing learning task based on RC template '" + rcSetting.getRcMachineTemplateId() + "' is about to be executed.");
		initRcMachineTemplateIfNeeded(rcSetting);
		if (!rcSetting.isRcMachineTemplateDefined()) {
    		throw new CoelRuntimeException("Reservoir machine template is missing for RC learning.");
		}

//		EvoTaskBO<?, ?, ?> evoTaskBO = EvoTaskBOFactory.getInstance().createInstance(taskDef);
//		evoTaskBO = createGridBasedEvoTaskBO(evoTaskBO);
//		GeneticAlgorithmBO<?, ?, ?> gaBO = GeneticAlgorithmBOFactory.getInstance().createInstance(taskDef, evoTaskBO);
//		log.info("Running the genetic algorithm for evo task '" + taskDef.getEvoTaskId() + "' with '" + taskDef.getEvoTask().getGaSetting().getGenerationLimit() + "' generation limit.");
//		gaBO.evolve();
		
		
		// TODO: Replace with DB storage		
	}

	private <M extends RcMachineTemplate> void initRcMachineTemplateIfNeeded(RcMachineTemplateHolder<M> holder) {
		if (holder.isRcMachineTemplateDefined()) {
			if (!holder.isRcMachineTemplateComplete()) {
				M rcMachineTemplate = (M) rcMachineTemplateDAO.get(holder.getRcMachineTemplate().getId());
				// FIX ME
//				if (rcMachineTemplate instanceof RcAcMachineTemplate) {
//					RcAcMachineTemplate rcMachineTemplate = (RcAcMachineTemplate) rcMachineTemplate;
//				}
				holder.setRcMachineTemplate(copyWithoutHibernate(rcMachineTemplate));
			}
		}
	}

	private <T> T copyWithoutHibernate(T object) {
		return hibernateReplicator.copy(object);
	}

	////////////////
	// INJECTIONS //
	////////////////

	public GenericDAO<RcMachineTemplate, Long> getRcMachineTemplateDAO() {
		return rcMachineTemplateDAO;
	}

	public void setRcMachineTemplateDAO(GenericDAO<RcMachineTemplate, Long> rcMachineTemplateDAO) {
		this.rcMachineTemplateDAO = rcMachineTemplateDAO;
	}

	public ComputationalGrid getGrid() {
		return grid;
	}

	public void setGrid(ComputationalGrid grid) {
		this.grid = grid;
	}

	public HibernateBeanReplicator getHibernateReplicator() {
		return hibernateReplicator;
	}

	public void setHibernateReplicator(HibernateBeanReplicator hibernateReplicator) {
		this.hibernateReplicator = hibernateReplicator;
	}
}