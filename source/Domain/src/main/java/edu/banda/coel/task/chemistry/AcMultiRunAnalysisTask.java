package edu.banda.coel.task.chemistry;

import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.core.domain.task.Task;
import com.banda.core.util.ObjectUtil;
import com.banda.math.domain.dynamics.MultiRunAnalysisSpec;
import edu.banda.coel.task.chemistry.AcTaskParts.AcMultipleCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcSimulationConfigHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.MultiRunAnalysisSpecHolder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2013
 */
public class AcMultiRunAnalysisTask extends Task implements AcMultipleCompartmentHolder, AcSimulationConfigHolder, MultiRunAnalysisSpecHolder<Double> {

    private Collection<AcCompartment> compartments;
    private AcSimulationConfig simulationConfig;
    private MultiRunAnalysisSpec<Double> spec;

    public AcMultiRunAnalysisTask() {
        super();
    }

    @Override
    public Collection<AcCompartment> getCompartments() {
        return compartments;
    }

    @Override
    public void setCompartments(Collection<AcCompartment> compartments) {
        this.compartments = compartments;
    }

    @Override
    public boolean areCompartmentsDefined() {
        return compartments != null && !compartments.isEmpty();
    }

    @Override
    public boolean areCompartmentsComplete() {
        return ObjectUtil.getFirst(compartments).getLabel() != null;
    }

    public void setCompartmentIds(Collection<Long> compartmentIds) {
        if (compartmentIds == null) {
            return;
        }
        compartments = new ArrayList<AcCompartment>();
        for (Long compartmentId : compartmentIds) {
            AcCompartment compartment = new AcCompartment();
            compartment.setId(compartmentId);
            compartments.add(compartment);
        }
    }

    public Collection<Long> getCompartmentIds() {
        if (!areCompartmentsDefined()) {
            return null;
        }
        Collection<Long> compartmentIds = new ArrayList<Long>();
        for (AcCompartment compartment : compartments) {
            compartmentIds.add(compartment.getId());
        }
        return compartmentIds;
    }

    @Override
    public AcSimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    @Override
    public void setSimulationConfig(AcSimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    public void setSimulationConfigId(Long simulationConfigId) {
        simulationConfig = new AcSimulationConfig();
        simulationConfig.setId(simulationConfigId);
    }

    @Override
    public boolean isSimulationConfigDefined() {
        return simulationConfig != null;
    }

    @Override
    public boolean isSimulationConfigComplete() {
        return simulationConfig.getOdeSolverType() != null;
    }

    @Override
    public MultiRunAnalysisSpec<Double> getMultiRunAnalysisSpec() {
        return spec;
    }

    @Override
    public void setMultiRunAnalysisSpec(MultiRunAnalysisSpec<Double> multiRunAnalysisSpec) {
        this.spec = multiRunAnalysisSpec;
    }

    @Override
    public boolean isMultiRunAnalysisSpecDefined() {
        return spec != null;
    }

    @Override
    public boolean isMultiRunAnalysisSpecComplete() {
        return spec.getRunNum() != null;
    }
}