package edu.banda.coel.business.evo.fitness

import java.{lang => jl,util => ju}
import scala.collection.JavaConversions._
import com.banda.chemistry.business.AcEvaluationBOFactory
import com.banda.chemistry.business.ChemistryRunnableFactory
import com.banda.chemistry.business.ChemistryInterpretationFactory
import com.banda.chemistry.domain.AcReaction.ReactionDirection
import com.banda.chemistry.domain.AcReactionSet
import edu.banda.coel.task.chemistry.AcRunAndTranslateTask
import com.banda.math.domain.evo.ArrayChromosome
import com.banda.chemistry.business.AcRateConstantUtil
import edu.banda.coel.domain.evo.EvoAcRateConstantTask
import edu.banda.coel.domain.evo.EvoAcInteractionSeriesTask
import com.banda.function.domain.Expression
import com.banda.function.domain.FunctionHolder
import com.banda.chemistry.domain.AcInteractionSeries
import com.banda.chemistry.business.AcReplicator
import java.util.HashMap

class EvoAcInteractionSeriesFitnessEvaluatorBO(
	evoTask : EvoAcInteractionSeriesTask,
	chemistryRunnableFactory : ChemistryRunnableFactory,
	chemInterpretationFactory : ChemistryInterpretationFactory,
	acEvaluationBOFactory : AcEvaluationBOFactory
	) extends EvoAcFitnessEvaluatorBO[ArrayChromosome[jl.Double]](
	evoTask, chemistryRunnableFactory, chemInterpretationFactory, acEvaluationBOFactory) {

    val interactionSeriesChromosomeConverter = new EvoChromosomeAcInteractionSeriesConverter(evoTask)

	override def adaptTask(task : AcRunAndTranslateTask, chromosome : ArrayChromosome[jl.Double]) {
        val originalInteractionSeries = task.getActionSeries
        // since we need to modify the original interaction series for thread safety we have to make a copy
        val newInteractionSeriesMap = interactionSeriesChromosomeConverter.toClone(originalInteractionSeries, chromosome.getCode)
        // replace the task's interaction series 
        task.getRunTaskDefinition().setActionSeries(newInteractionSeriesMap.get(originalInteractionSeries))
	}
}

class EvoChromosomeAcInteractionSeriesConverter(evoTask : EvoAcInteractionSeriesTask) {

	private val interactionSeriesBounds = {
    	def collectInteractionSeries(interactionSeries : Iterable[AcInteractionSeries]) : Iterable[AcInteractionSeries] =
        	interactionSeries ++ interactionSeries.map(a => collectInteractionSeries(a.getSubActionSeries())).flatten

        val speciesAssignmentBoundMap = evoTask.getSpeciesAssignmentBoundMap
        val variableAssignmentBoundMap = evoTask.getVariableAssignmentBoundMap

        collectInteractionSeries(evoTask.getActionSeries).toSeq.sortBy(_.getId).map(actionSeries =>
        	(actionSeries,
        		actionSeries.getActions.toList.map(action => {
        		val variableAssignments = action.getVariableAssignments().toSeq.sortBy(_.getId).filter(variableAssignmentBoundMap.containsKey(_))  
        		val speciesActions = action.getSpeciesActions().toSeq.sortBy(_.getId).filter(speciesAssignmentBoundMap.containsKey(_))

        		(variableAssignments.map(a => (a, variableAssignmentBoundMap.get(a)))
        			++ speciesActions.map(a => (a, speciesAssignmentBoundMap.get(a))))
        		}).flatten
        	)
        )
    }

	private val interactionSeriesBoundMap = interactionSeriesBounds.toMap

    private val boundIndexMap = interactionSeriesBounds.map(_._2.map(_._2)).flatten.distinct.zipWithIndex.toMap

    def toClone(
        originalInteractionSeries : AcInteractionSeries,
        code : Array[jl.Double]
    ) : ju.Map[AcInteractionSeries, AcInteractionSeries] = {
        val interactionSeriesCloneMap = new HashMap[AcInteractionSeries, AcInteractionSeries]
        AcReplicator.getInstance().cloneActionSeriesWithActionsRecursively(originalInteractionSeries, interactionSeriesCloneMap)

        setAssignmentValueRecursively(originalInteractionSeries)

        def setAssignmentValueRecursively(interactionSeries : AcInteractionSeries) {
            val interactionSeriesClone = interactionSeriesCloneMap.get(interactionSeries)
        	interactionSeriesBoundMap.get(interactionSeries).get.foreach{
            	case (x,bound) => {
            	    val assignments = interactionSeriesClone.getActions().map{a => a.getSpeciesActions() ++ a.getVariableAssignments()}.flatten
               	    // works because of domain object class&id based 'equals' definition
            	    val matchedAssignment = assignments.find(_.equals(x))
            	    if (matchedAssignment.isDefined) 
            	    	matchedAssignment.get.setFunction(Expression.Double("" + code(boundIndexMap.get(bound).get)))}
            	}
        	interactionSeries.getSubActionSeries().foreach(setAssignmentValueRecursively)
        }
        interactionSeriesCloneMap
	}

	def modify(
        interactionSeries : AcInteractionSeries,
        code : Array[jl.Double]
    ) = {
        setAssignmentValueRecursively(interactionSeries)

        def setAssignmentValueRecursively(interactionSeries : AcInteractionSeries) {
        	interactionSeriesBoundMap.get(interactionSeries).get.foreach{
            	case (x,bound) => x.setFunction(Expression.Double("" + code(boundIndexMap.get(bound).get)))}
        	interactionSeries.getSubActionSeries().foreach(setAssignmentValueRecursively)
        }
	}
}