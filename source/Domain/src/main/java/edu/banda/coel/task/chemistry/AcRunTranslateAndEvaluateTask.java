package edu.banda.coel.task.chemistry;

/**
 * @author Peter Banda
 * @since 2011
 */
public class AcRunTranslateAndEvaluateTask extends AcCoreEvaluateTask {

	private AcRunAndTranslateTask runAndTranslationTaskDefinition;

	public AcRunAndTranslateTask getRunAndTranslationTaskDefinition() {
		return runAndTranslationTaskDefinition;
	}

	public void setRunAndTranslationTaskDefinition(AcRunAndTranslateTask runAndTranslationTaskDefinition) {
		this.runAndTranslationTaskDefinition = runAndTranslationTaskDefinition;
	}
}