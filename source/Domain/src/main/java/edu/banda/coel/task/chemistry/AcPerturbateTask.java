package edu.banda.coel.task.chemistry;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcPerturbateTask extends AcRunTranslateAndEvaluateTask {

    private Double perturbationStrength;
    private Integer repetitions;

    public Double getPerturbationStrength() {
        return perturbationStrength;
    }

    public void setPerturbationStrength(Double perturbationStrength) {
        this.perturbationStrength = perturbationStrength;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }
}