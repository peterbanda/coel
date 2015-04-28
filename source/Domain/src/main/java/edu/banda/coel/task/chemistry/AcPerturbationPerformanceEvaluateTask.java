package edu.banda.coel.task.chemistry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcPerturbationPerformanceEvaluateTask extends AcPerformanceEvaluateTask {

    private Integer perturbationNum;
    private Collection<Double> perturbationStrengths = new ArrayList<Double>();

    public Integer getPerturbationNum() {
        return perturbationNum;
    }

    public void setPerturbationNum(Integer perturbationNum) {
        this.perturbationNum = perturbationNum;
    }

    public Collection<Double> getPerturbationStrengths() {
        return perturbationStrengths;
    }

    public void setPerturbationStrengths(Collection<Double> perturbationStrengths) {
        this.perturbationStrengths = perturbationStrengths;
    }

    public void addPerturbationStrength(Double perturbationStrength) {
        perturbationStrengths.add(perturbationStrength);
    }
}