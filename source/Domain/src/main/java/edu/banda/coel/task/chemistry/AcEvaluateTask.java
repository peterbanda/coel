package edu.banda.coel.task.chemistry;

import com.banda.chemistry.BndChemistryException;
import com.banda.chemistry.domain.AcCompartment;
import com.banda.chemistry.domain.AcSimulationConfig;
import com.banda.chemistry.domain.AcTranslatedRun;
import com.banda.chemistry.domain.AcTranslationSeries;
import com.banda.core.util.ObjectUtil;
import edu.banda.coel.task.chemistry.AcTaskParts.AcCompartmentHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcSimulationConfigHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcTranslatedRunsHolder;
import edu.banda.coel.task.chemistry.AcTaskParts.AcTranslationSeriesHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcEvaluateTask extends AcCoreEvaluateTask implements AcCompartmentHolder, AcSimulationConfigHolder, AcTranslationSeriesHolder, AcTranslatedRunsHolder {

    private AcCompartment compartment;
    private AcSimulationConfig simulationConfig;
    private AcTranslationSeries translationSeries;

    private Collection<AcTranslatedRun> translatedRuns;

    @Override
    public AcCompartment getCompartment() {
        return compartment;
    }

    @Override
    public void setCompartment(AcCompartment compartment) {
        this.compartment = compartment;
    }

    @Override
    public boolean isCompartmentDefined() {
        return compartment != null;
    }

    @Override
    public boolean isCompartmentComplete() {
        return isCompartmentDefined() && compartment.getReactionSet() != null;
    }

    public void setCompartmenId(Long compartmentId) {
        compartment = new AcCompartment();
        compartment.setId(compartmentId);
    }

    public Long getCompartmentId() {
        if (isCompartmentDefined()) {
            return compartment.getId();
        }
        return null;
    }

    @Override
    public AcSimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    @Override
    public void setSimulationConfig(AcSimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
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
    public Collection<AcTranslatedRun> getTranslatedRuns() {
        return translatedRuns;
    }

    @Override
    public void setTranslatedRuns(Collection<AcTranslatedRun> translatedRuns) {
        this.translatedRuns = translatedRuns;
    }

    @Override
    public boolean areTranslatedRunsDefined() {
        return translatedRuns != null && !translatedRuns.isEmpty();
    }

    @Override
    public boolean areTranslatedRunsComplete() {
        return ObjectUtil.getFirst(translatedRuns).getCreateTime() != null;
    }

    public void setTranslatedRunIdsAsStrings(Collection<String> translatedRunStringIds) {
        Collection<Long> translatedRunIds = new ArrayList<Long>();
        for (String translatedRunStringId : translatedRunStringIds) {
            translatedRunIds.add(Long.parseLong(translatedRunStringId));
        }
        setTranslatedRunIds(translatedRunIds);
    }

    public void setTranslatedRunIds(Collection<Long> translatedRunIds) {
        if (translatedRunIds == null) {
            return;
        }
        if (translatedRuns != null) {
            throw new BndChemistryException("AC traslated run(s) already set for AC evaluation task.");
        }
        translatedRuns = new ArrayList<AcTranslatedRun>();
        for (Long traslatedRunId : translatedRunIds) {
            translatedRuns.add(new AcTranslatedRun(traslatedRunId));
        }
    }

    @Override
    public Collection<Long> getTranslatedRunIds() {
        if (!areTranslatedRunsDefined()) {
            return null;
        }
        Collection<Long> translatedRunIds = new ArrayList<Long>();
        for (AcTranslatedRun translatedRun : translatedRuns) {
            translatedRunIds.add(translatedRun.getId());
        }
        return translatedRunIds;
    }

    public void setTranslatedRun(AcTranslatedRun translatedRun) {
        this.translatedRuns = Collections.singleton(translatedRun);
    }

    public void setTranslatedRunId(Long translatedRunId) {
        setTranslatedRunIds(Collections.singleton(translatedRunId));
    }

    @Override
    public AcTranslationSeries getTranslationSeries() {
        return translationSeries;
    }

    @Override
    public void setTranslationSeries(AcTranslationSeries translationSeries) {
        this.translationSeries = translationSeries;
    }

    @Override
    public boolean isTranslationSeriesDefined() {
        return translationSeries != null;
    }

    @Override
    public boolean isTranslationSeriesComplete() {
        return !translationSeries.getTranslations().isEmpty();
    }

    public void setTranslationSeriesId(Long translationSeriesId) {
        if (translationSeriesId == null) {
            return;
        }
        if (translationSeries != null) {
            throw new BndChemistryException("AC translation series already set for AC evaluation task.");
        }
        translationSeries = new AcTranslationSeries();
        translationSeries.setId(translationSeriesId);
    }

    public Long getTranslationSeriesId() {
        return isTranslationSeriesDefined() ? translationSeries.getId() : null;
    }
}