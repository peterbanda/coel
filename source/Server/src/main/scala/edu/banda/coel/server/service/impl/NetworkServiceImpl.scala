package edu.banda.coel.server.service.impl

import java.{util => ju, lang => jl}

import com.banda.serverbase.grid.ArgumentCallable

import scala.collection.JavaConversions._
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.transaction.annotation.Transactional
import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.task.network.NetworkRunTask

import com.banda.core.domain.ComponentRunTrace
import edu.banda.coel.server.grid.callable.GridNetworkRunAndEvaluateCallFactory
import com.banda.network.domain.TopologicalNode
import edu.banda.coel.task.network.NetworkTaskParts.NetworkHolder
import edu.banda.coel.task.network.NetworkTaskParts.NetworkSimulationConfigHolder
import edu.banda.coel.task.network.NetworkTaskParts.NetworkMultipleEvaluationHolder
import edu.banda.coel.task.network.NetworkTaskParts.NetworkMultipleActionSeriesHolder
import edu.banda.coel.task.network.NetworkPerformanceEvaluateTask
import edu.banda.coel.task.network.SpatialNetworkPerformanceEvaluateTask
import org.springframework.beans.factory.annotation.Autowired
import edu.banda.coel.server.dao.ArtificialChemistryDAO
import com.banda.serverbase.persistence.GenericDAO
import com.banda.network.domain.Network
import net.sf.beanlib.hibernate.HibernateBeanReplicator
import com.banda.network.domain.NetworkSimulationConfig
import org.apache.commons.logging.LogFactory
import edu.banda.coel.task.network.NetworkRunAndEvaluateTask
import edu.banda.coel.task.network.NetworkPerformanceEvaluateTask
import org.springframework.transaction.annotation.Propagation
import edu.banda.coel.task.network.NetworkRunAndEvaluateTask
import scala.collection.mutable.Buffer
import edu.banda.coel.task.network.NetworkRunAndEvaluateTask
import com.banda.math.business.MathUtil
import com.banda.network.domain.NetworkPerformance
import com.banda.core.domain.um.User
import com.banda.network.domain.NetworkActionSeries
import com.banda.network.domain.NetworkEvaluation
import edu.banda.coel.CoelRuntimeException
import com.banda.math.domain.Stats
import com.banda.network.domain.SpatialNetworkPerformance
import com.banda.network.domain.SpatialTopology
import java.util.Collections
import com.banda.math.domain.StatsSequence
import com.banda.network.domain.AbstractNetworkPerformance
import org.springframework.beans.factory.annotation.Qualifier
import com.banda.network.domain.NetworkDerridaAnalysis
import com.banda.network.domain.NetworkDamageSpreading

/**
 * Title: NetworkServiceImpl
 *
 * @see edu.banda.coel.domain.service.NetworkService
 * @author Peter Banda
 * @since 2011
 */
private class NetworkServiceImpl extends AbstractService with NetworkService {

  @Autowired
  private val networkDAO: GenericDAO[Network[_], jl.Long] = null

  @Autowired
  private val networkActionSeriesDAO: GenericDAO[NetworkActionSeries[_], jl.Long] = null

  @Autowired
  private val networkEvaluationDAO: GenericDAO[NetworkEvaluation, jl.Long] = null

  @Autowired
  @Qualifier("networkPerformanceDAO")
  private val networkPerformanceDAO: GenericDAO[AbstractNetworkPerformance[_], jl.Long] = null

  @Autowired
  private val networkDerridaAnalysisDAO: GenericDAO[NetworkDerridaAnalysis[_], jl.Long] = null

  @Autowired
  private val networkDamageSpreadingDAO: GenericDAO[NetworkDamageSpreading[_], jl.Long] = null

  @Autowired
  private val networkRunCall: ArgumentCallable[NetworkRunTask[_], ComponentRunTrace[_, TopologicalNode]] = null

  @Autowired
  private val networkRunAndEvaluateCallFactory: GridNetworkRunAndEvaluateCallFactory = null

  @Autowired
  private val networkBooleanDerridaAnalysisCall: ArgumentCallable[NetworkRunTask[jl.Boolean], Iterable[(Double, Double)]] = null

  @Autowired
  private val networkBooleanDamageSpreadingAnalysisCall: ArgumentCallable[NetworkRunTask[jl.Boolean], Iterable[(Double, Double)]] = null

  //    @Autowired
  //    private val networkSimulationConfigDAO : GenericDAO[NetworkSimulationConfig, jl.Long] = null

  //////////////
  // SERVICES //
  //////////////

  /**
   * @see edu.banda.coel.domain.service.NetworkService#runSimulation(NetworkRunTask)
   */
  @Transactional(readOnly = true)
  override def runSimulation[T](task: NetworkRunTask[T]): ju.Collection[ComponentRunTrace[T, TopologicalNode]] = {
    log.info("Network run simulation for network '" + task.getNetworkId + "' is about to be executed.")

    initNetworkIfNeeded(task)
    val runTraces = computationalGrid.runSerialLocal(networkRunCall.asInstanceOf[ArgumentCallable[NetworkRunTask[T], ComponentRunTrace[T, TopologicalNode]]], task, task.getRepetitions)

    log.info("Network run simulation for network '" + task.getNetworkId + "' finished.")
    runTraces
  }

  /**
   * @see edu.banda.coel.domain.service.NetworkService#runPerformanceEvaluation(NetworkPerformanceEvaluateTask)
   */
  @Transactional
  override def runPerformanceEvaluation[T](task: NetworkPerformanceEvaluateTask[T]) = {
    log.info("Network performance evaluation for network '" + task.getNetworkId + "' and evaluations '" + task.getNetworkEvaluationIds + "' is about to be executed.")

    initNetworkIfNeeded(task)
    initMultipleNetworkActionSeriesIfNeeded(task)
    initMultipleNetworkEvaluationsIfNeeded(task)
    if (task.getRepetitions == null)
      throw new CoelRuntimeException("Repetitions missing for network performance evaluation task.")

    val networkRunAndEvaluateCall = networkRunAndEvaluateCallFactory.apply(task.getNetwork.getStateClazz)
    val sortedActionSeries = new ju.ArrayList[NetworkActionSeries[T]](task.getNetworkActionSeries()).sortBy(_.getId())
    val sortedEvaluations = task.getNetworkEvaluations.toSeq.sortBy(_.getId())

    for (actionSeries <- sortedActionSeries)
      runMultiPerformanceEvaluation(networkRunAndEvaluateCall, task, actionSeries, sortedEvaluations)
  }

  /**
   * @see edu.banda.coel.domain.service.NetworkService#runSpatialPerformanceEvaluation(SpatialNetworkPerformanceEvaluateTask)
   */
  @Transactional
  override def runSpatialPerformanceEvaluation[T](task: SpatialNetworkPerformanceEvaluateTask[T]) = {
    log.info("Spatial network performance evaluation for network '" + task.getNetworkId + "' evaluations '" + task.getNetworkEvaluationIds + "', and size from-to '" + task.getSizeFrom + "-" + task.getSizeTo + "' is about to be executed.")

    initNetworkIfNeeded(task)
    initMultipleNetworkActionSeriesIfNeeded(task)
    initMultipleNetworkEvaluationsIfNeeded(task)
    if (task.getRepetitions == null)
      throw new CoelRuntimeException("Repetitions missing for spatial network performance evaluation task.")
    if (task.getSizeFrom == null)
      throw new CoelRuntimeException("Size from missing for spatial network performance evaluation task.")
    if (task.getSizeTo == null)
      throw new CoelRuntimeException("Size to missing for spatial network performance evaluation task.")

    val networkRunAndEvaluateCall = networkRunAndEvaluateCallFactory.apply(task.getNetwork.getStateClazz)
    val sortedActionSeries = new ju.ArrayList[NetworkActionSeries[T]](task.getNetworkActionSeries()).sortBy(_.getId())
    val sortedEvaluations = task.getNetworkEvaluations.toSeq.sortBy(_.getId())

    for (actionSeries <- sortedActionSeries)
      runMultiSpatialPerformanceEvaluation(networkRunAndEvaluateCall, task, actionSeries, sortedEvaluations)
  }

  /**
   * @see edu.banda.coel.domain.service.NetworkService#runBooleanDerridaAnalysis(NetworkRunTask)
   */
  @Transactional
  override def runBooleanDerridaAnalysis(task: NetworkRunTask[jl.Boolean]) = {
    log.info("Boolean Derrida analysis for network '" + task.getNetworkId + "' is about to be executed.")

    initNetworkIfNeeded(task)
    val results = computationalGrid.runOnGridSync(networkBooleanDerridaAnalysisCall, task, 1, task.getJobsInSequenceNum, task.getMaxJobsInParallelNum)
    val result = results.head
    val maxDistance = result.maxBy(_._1)._1

    val normalizedStats = result.groupBy { case (a, b) => a }.map {
      case (a, all) => (a / maxDistance, MathUtil.calcStats(a / maxDistance, all.map(_._2 / maxDistance)))
    }: Iterable[(Double, Stats)]

    val sortedNormalizedStats = normalizedStats.toSeq.sortBy(_._1).map {
      _._2
    }
    val statsSeq = new StatsSequence
    statsSeq.setStats(new ju.ArrayList(sortedNormalizedStats))

    val derridaAnalysis = new NetworkDerridaAnalysis[jl.Boolean]
    derridaAnalysis.setNetwork(task.getNetwork)
    derridaAnalysis.setRepetitions(task.getRepetitions)
    derridaAnalysis.setRunTime(task.getRunTime)
    derridaAnalysis.setResult(statsSeq)

    val user = new User()
    user.setId(1l)
    derridaAnalysis.setCreatedBy(user)
    val savedDerridaAnalysis = networkDerridaAnalysisDAO.save(derridaAnalysis)

    postAndLogSaveMessageForKey(savedDerridaAnalysis.getId, "Network Derrida Analysis")
  }

  /**
   * @see edu.banda.coel.domain.service.NetworkService#runBooleanDamageSpreadingAnalysis(NetworkRunTask)
   */
  @Transactional
  override def runBooleanDamageSpreadingAnalysis(task: NetworkRunTask[jl.Boolean]) = {
    log.info("Boolean damage spreading analysis for network '" + task.getNetworkId + "' is about to be executed.")

    initNetworkIfNeeded(task)
    val results = computationalGrid.runOnGridSync(networkBooleanDamageSpreadingAnalysisCall, task, 1, task.getJobsInSequenceNum, task.getMaxJobsInParallelNum)
    val result = results.head

    val stats = result.groupBy { case (a, b) => a }.map {
      case (a, all) => (a, MathUtil.calcStats(a, all.map(_._2)))
    }: Iterable[(Double, Stats)]

    val sortedStats = stats.toSeq.sortBy(_._1).map {
      _._2
    }
    val statsSeq = new StatsSequence
    statsSeq.setStats(new ju.ArrayList(sortedStats))

    val damageSpreading = new NetworkDamageSpreading[jl.Boolean]
    damageSpreading.setNetwork(task.getNetwork)
    damageSpreading.setRepetitions(task.getRepetitions)
    damageSpreading.setRunTime(task.getRunTime)
    damageSpreading.setResult(statsSeq)

    val user = new User()
    user.setId(1l)
    damageSpreading.setCreatedBy(user)
    val savedDamageSpreading = networkDamageSpreadingDAO.save(damageSpreading)

    postAndLogSaveMessageForKey(savedDamageSpreading.getId, "Network Damage Spreading")
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def runMultiPerformanceEvaluation[T](
                                        networkRunAndEvaluateCall: ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]],
                                        task: NetworkPerformanceEvaluateTask[T],
                                        actionSeries: NetworkActionSeries[T],
                                        evaluations: Seq[NetworkEvaluation]
                                        ) = {
    val stats = runAndEvaluate(networkRunAndEvaluateCall, task, task.getNetwork, actionSeries, evaluations)

    val savedPerformances = (stats, evaluations).zipped.map { (result, evaluation) => {
      // create a new evaluated performance and fill it with data
      val performance = new NetworkPerformance[T]
      performance.setTimeCreated(new ju.Date())
      performance.setNetwork(task.getNetwork)
      performance.setInteractionSeries(actionSeries)
      performance.setEvaluation(evaluation)
      performance.setRepetitions(task.getRepetitions)
      performance.setRunTime(task.getRunTime)
      performance.setResult(result)

      val user = new User()
      user.setId(1l)
      performance.setCreatedBy(user)

      networkPerformanceDAO.save(performance)
    }
    }
    postAndLogSaveMessageForKeys(savedPerformances.map(_.getId), "Network evaluated performance")
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  def runMultiSpatialPerformanceEvaluation[T](
                                               networkRunAndEvaluateCall: ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]],
                                               task: SpatialNetworkPerformanceEvaluateTask[T],
                                               actionSeries: NetworkActionSeries[T],
                                               evaluations: Seq[NetworkEvaluation]
                                               ) = {
    val network = task.getNetwork
    val topology = network.getTopology.asInstanceOf[SpatialTopology]
    val dims = topology.getSizes.size

    val results = for (size <- (task.getSizeFrom: Int) to task.getSizeTo) yield {
      // set size for given dims to new (cloned) network / topology
      val newNetwork = genericReflectionProvider.clone(network)
      val newTopology = genericReflectionProvider.clone(topology)
      newTopology.setSizes(new ju.ArrayList[Integer](Collections.nCopies(dims, size: jl.Integer)))
      newNetwork.setTopology(newTopology)

      val stats: Iterable[Stats] = runAndEvaluate(networkRunAndEvaluateCall, task, newNetwork, actionSeries, evaluations)
      //	    	stats.setPos(size - task.getSizeFrom)
      stats
    }
    val savedPerformances = (results.transpose, evaluations).zipped.map { (result, evaluation) => {
      result.zipWithIndex.foreach { case (stat, index) => stat.setPos(index) }

      val statsSeq = new StatsSequence
      statsSeq.setStats(new ju.ArrayList(result))

      // create a new evaluated performance and fill it with data
      val performance = new SpatialNetworkPerformance[T]
      performance.setTimeCreated(new ju.Date())
      performance.setNetwork(task.getNetwork)
      performance.setInteractionSeries(actionSeries)
      performance.setEvaluation(evaluation)
      performance.setRepetitions(task.getRepetitions)
      performance.setRunTime(task.getRunTime)
      performance.setSizeFrom(task.getSizeFrom)
      performance.setSizeTo(task.getSizeTo)
      performance.setResults(statsSeq)

      val user = new User()
      user.setId(1l)
      performance.setCreatedBy(user)

      networkPerformanceDAO.save(performance)
    }
    }
    postAndLogSaveMessageForKeys(savedPerformances.map(_.getId), "Spatial network evaluated performance")
  }

  private def runAndEvaluate[T](
                                 networkRunAndEvaluateCall: ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]],
                                 task: NetworkPerformanceEvaluateTask[T],
                                 network: Network[T],
                                 actionSeries: NetworkActionSeries[T],
                                 evaluations: Seq[NetworkEvaluation]
                                 ): Iterable[Stats] = {
    // create and run a 'run and evaluate task'
    val runAndEvaluateTask = new NetworkRunAndEvaluateTask[T]
    val runTask = new NetworkRunTask[T]
    runTask.setNetwork(network)
    runTask.setActionSeries(actionSeries)
    runTask.setRepetitions(task.getRepetitions)
    runTask.setRunTime(task.getRunTime)
    runTask.setSimulationConfig(task.getSimulationConfig)

    runAndEvaluateTask.setRunTask(runTask)
    runAndEvaluateTask.setNetworkEvaluations(evaluations)
    runAndEvaluateTask.setJobsInSequenceNum(task.getJobsInSequenceNum)
    runAndEvaluateTask.setMaxJobsInParallelNum(task.getMaxJobsInParallelNum)

    val results = runAndEvaluate(networkRunAndEvaluateCall, runAndEvaluateTask)

    // calc stats
    MathUtil.calcStatsBySecond(results)
  }

  @Transactional
  def runAndEvaluate[T](
                         networkRunAndEvaluateCall: ArgumentCallable[NetworkRunAndEvaluateTask[T], Iterable[Double]],
                         task: NetworkRunAndEvaluateTask[T]
                         ): ju.Collection[Iterable[Double]] = {
    log.info("Network run and evaluation for network '" + task.getNetworkId() + "', interaction series '" + task.getActionSeriesId() + "', and evaluations '" + task.getNetworkEvaluationIds() + "' is about to be executed.")

    // Run on computational grid or locally
    val repetitions = task.getRunTask.getRepetitions
    val results = if (task.isRunOnGrid())
      computationalGrid.runOnGridSync(networkRunAndEvaluateCall, task, repetitions, task.getJobsInSequenceNum(), task.getMaxJobsInParallelNum())
    else
      computationalGrid.runSerialLocal(networkRunAndEvaluateCall, task, repetitions)
    log.info("Network run and evaluation for network '" + task.getNetworkId() + "', interaction series '" + task.getActionSeriesId() + "', and evaluations '" + task.getNetworkEvaluationIds() + "' finished.")
    results
  }

  @Transactional
  private def initNetworkIfNeeded[T](holder: NetworkHolder[T]) =
    if (holder.isNetworkDefined) {
      var network = holder.getNetwork
      if (!holder.isNetworkComplete) {
        network = networkDAO.get(network.getId).asInstanceOf[Network[T]]
        // lazy initialization
        loadLazyPropsOfNetwork(network)
      }
      val newNetwork = copyWithoutHibernate(network)
      holder.setNetwork(newNetwork)
    }

  @Transactional
  private def initNetworkSimulationConfigIfNeeded[T](holder: NetworkSimulationConfigHolder) =
    if (holder.isSimulationConfigDefined) {
      var networkSimulationConfig = holder.getSimulationConfig
      if (!holder.isSimulationConfigComplete) {
        // networkSimulationConfig = networkSimulationConfigDAO.get(networkSimulationConfig.getId)
      }
      holder.setSimulationConfig(copyWithoutHibernate(networkSimulationConfig))
    }

  @Transactional
  private def initMultipleNetworkActionSeriesIfNeeded[T](holder: NetworkMultipleActionSeriesHolder[T]) =
    if (holder.areNetworkActionSeriesDefined) {
      val actionSeries = if (holder.areNetworkActionSeriesComplete)
        holder.getNetworkActionSeries
      else
        networkActionSeriesDAO.get(holder.getNetworkActionSeriesIds.asInstanceOf[ju.Collection[jl.Long]]).asInstanceOf[ju.Collection[NetworkActionSeries[T]]]
      // lazy init
      actionSeries.foreach(_.getActions.size)
      holder.setNetworkActionSeries(copyWithoutHibernate(actionSeries))
    }

  @Transactional
  private def initMultipleNetworkEvaluationsIfNeeded(holder: NetworkMultipleEvaluationHolder) =
    if (holder.areNetworkEvaluationsDefined) {
      val evaluations = if (holder.areNetworkEvaluationsComplete)
        holder.getNetworkEvaluations
      else
        networkEvaluationDAO.get(holder.getNetworkEvaluationIds.asInstanceOf[ju.Collection[jl.Long]]).asInstanceOf[ju.Collection[NetworkEvaluation]]
      //	holder.getNetworkEvaluationIds.map{id : jl.Long => networkEvaluationDAO.get(id)} : ju.Collection[NetworkEvaluation]
      // lazy init
      val evals = evaluations.filterNot {
        var set = Set[jl.Long]()
        obj => val b = set(obj.getId); set += obj.getId; b
      }
      evals.foreach(_.getEvaluationItems.size)
      holder.setNetworkEvaluations(copyWithoutHibernate(new ju.ArrayList(evals)))
    }

  private def loadLazyPropsOfNetwork[T](network: Network[T]) {
    network.getWeightSetting
//    if (network.getTopology.isSpatial) {
//      // neighborhood
//      network.getTopology.asInstanceOf[SpatialTopology].getNeighborhood.getName
//    }
  }
}