package edu.banda.coel.task;

import edu.banda.coel.domain.rc.RcMachineTemplate;

/**
 * @author Peter Banda
 * @since 2012
 */
public interface RcTaskParts {

	public interface RcMachineTemplateHolder<M extends RcMachineTemplate> {
		public M getRcMachineTemplate();
		public void setRcMachineTemplate(M rcMachineTemplate);
		public boolean isRcMachineTemplateDefined();
		public boolean isRcMachineTemplateComplete();
	}
}