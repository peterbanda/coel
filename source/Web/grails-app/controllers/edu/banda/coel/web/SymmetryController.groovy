package edu.banda.coel.web

import com.banda.core.domain.MultiStateUpdateType
import com.banda.core.dynamics.StateAlternationType
import com.banda.core.metrics.MetricsType
import com.banda.core.parallel.Parallelizer
import com.banda.core.parallel.RunnableWith
import com.banda.core.util.ObjectUtil
import com.banda.function.business.FunctionFactory
import com.banda.function.domain.Function
import com.banda.function.enumerator.ListEnumeratorFactory
import com.banda.math.business.sym.twodim.TwoDimSymmetricConfigurationEnumerator
import com.banda.network.business.TopologyFactory
import com.banda.network.domain.Network
import com.banda.network.domain.NetworkAction
import com.banda.network.domain.NetworkActionSeries
import com.banda.network.domain.NetworkFunction
import com.banda.network.domain.SpatialTopology
import com.banda.network.domain.Topology
import edu.banda.coel.business.netpic.JavaNetworkRunSpatialPicGenerator
import edu.banda.coel.domain.TwoDimSymCARunSpec
import edu.banda.coel.domain.TwoDimSymConfSpec
import edu.banda.coel.domain.service.NetworkService
import edu.banda.coel.task.network.NetworkRunTask
import grails.converters.JSON

import java.math.MathContext
import java.math.RoundingMode;
import java.text.DecimalFormat
import java.util.concurrent.ConcurrentLinkedQueue

class SymmetryController extends BaseController {

    def NetworkService networkService

    def TopologyFactory topologyFactory

    def ListEnumeratorFactory listEnumeratorFactory

    def functionFactory = new FunctionFactory()


    def mathContext = new MathContext(68, RoundingMode.HALF_EVEN)

    def threadsNum = Math.min(Runtime.getRuntime().availableProcessors(), 32)

    def index = {}

	def enumeration = {}

	def probability = {}

    def simulation = {}

    def enumerateUniformTwoDimConfs() {
        TwoDimSymConfSpec spec = bindParamsToSpec()

        if (!spec.validate()) {
            def errorsJSON = spec.errors as JSON
            render errorsJSON
            return
        }

        def enumerator = new TwoDimSymmetricConfigurationEnumerator(spec.latticeSize, spec.alphabetSize, listEnumeratorFactory)
        def count = enumerator.enumerateAll3()

        def formatter = new DecimalFormat("0.0E0")
        formatter.setMaximumIntegerDigits(1)
        formatter.setMaximumFractionDigits(2)

        def model = [full: count.toString(), exp: formatter.format(new BigDecimal(count))] as JSON
        render model
    }

    private def validateLatticeSize100(TwoDimSymConfSpec spec) {
        if (spec.latticeSize && spec.latticeSize > 100) {
            def message = message(code: 'edu.banda.coel.domain.TwoDimSymConfSpec.latticeSize.density_uniform.range.error', args: [spec.latticeSize, '1', '100'])
            spec.errors.rejectValue("latticeSize",null,message)
        }
    }

    def enumerateDensityUniformTwoDimConfs() {
        TwoDimSymConfSpec spec = bindParamsToSpec()

        spec.validate();
        validateLatticeSize100(spec)

        if (spec.hasErrors()) {
            def errorsJSON = spec.errors as JSON
            render errorsJSON
            return
        }

        def enumerator = new TwoDimSymmetricConfigurationEnumerator(spec.latticeSize, spec.alphabetSize, listEnumeratorFactory)

        def count = BigInteger.ZERO;
        for (int activeCellsNum = 0; activeCellsNum <= spec.latticeSize * initspec.latticeSizeSize; activeCellsNum++) {
            final BigInteger partialCount = enumerator.enumerate3(activeCellsNum)
            count = count.add(partialCount)
        }

        def formatter = new DecimalFormat("0.0E0")
        formatter.setMaximumIntegerDigits(1)
        formatter.setMaximumFractionDigits(2)

        def model = [full: count.toString(), exp: formatter.format(new BigDecimal(count))] as JSON
        render model
    }

    def calcTwoDimProbability() {
        TwoDimSymConfSpec spec = bindParamsToSpec()
        def distributionType = params["distributionType"]

        spec.validate();

        if (distributionType.equals("Density-Uniform")) {
            validateLatticeSize100(spec)
        }

        if (spec.hasErrors()) {
            def errorsJSON = spec.errors as JSON
            render errorsJSON
            return
        }

        if (distributionType.equals("Uniform")) {
            render calcUniformTwoDimProbability(spec)
        } else {
            render calcDensityUniformTwoDimProbability(spec)
        }
    }

    private def calcUniformTwoDimProbability(TwoDimSymConfSpec spec) {
        def enumerator = new TwoDimSymmetricConfigurationEnumerator(spec.latticeSize, spec.alphabetSize, listEnumeratorFactory)
        def count = enumerator.enumerateAll3()

        final BigInteger alphabet = BigInteger.valueOf(spec.alphabetSize)
        final BigInteger total = alphabet.pow(spec.latticeSize * spec.latticeSize)
        BigDecimal ratio = new BigDecimal(count).divide(new BigDecimal(total), mathContext)

        def formatter = new DecimalFormat("0.00E0")
//        formatter.setMaximumIntegerDigits(1)
//        formatter.setMaximumFractionDigits(2)

        def model = [full: ratio.toString(), exp: formatter.format(ratio)] as JSON
        model
    }

    private def calcDensityUniformTwoDimProbability(TwoDimSymConfSpec spec) {
        Collection<Integer> activeCellsNums = new ArrayList<Integer>()

        for (int activeCellsNum = 0; activeCellsNum <= spec.latticeSize * spec.latticeSize; activeCellsNum++) {
            activeCellsNums.add(activeCellsNum);
        }

        def results = new ConcurrentLinkedQueue<BigDecimal>();
        Parallelizer<Object> parallelizer = new Parallelizer<Integer>(
            createTwoDimDensityUniformProb(results, spec.latticeSize, spec.alphabetSize),
            threadsNum,
            activeCellsNums
        )
        parallelizer.run()

        BigDecimal ratioSum = BigDecimal.ZERO
        for (BigDecimal result : results) {
            ratioSum = ratioSum.add(result)
        }

        BigDecimal ratio = ratioSum.divide(BigDecimal.valueOf(spec.latticeSize * spec.latticeSize + 1), mathContext);

        def formatter = new DecimalFormat("0.00E0")
//        formatter.setMaximumIntegerDigits(1)
//        formatter.setMaximumFractionDigits(2)

        def model = [full: ratio.toString(), exp: formatter.format(ratio)] as JSON
        model
    }

    private def RunnableWith<Integer> createTwoDimDensityUniformProb(Collection<BigDecimal> results, Integer size, Integer alphabetSize) {
        return new RunnableWith<Integer>() {

            final enumerator = new TwoDimSymmetricConfigurationEnumerator(size, alphabetSize, listEnumeratorFactory)
            final BigInteger alphabetMinusOne = BigInteger.valueOf(alphabetSize - 1)

            @Override
            public void run(Integer activeCellsNum) {
                BigInteger nonActiveTotal = alphabetMinusOne.pow(size * size - activeCellsNum);
                final BigInteger count = enumerator.enumerate3(activeCellsNum);
                if (count.doubleValue() != 0) {
                    BigInteger total = binomial(size * size, activeCellsNum).multiply(nonActiveTotal)
                    BigDecimal partialRatio = new BigDecimal(count).divide(new BigDecimal(total), mathContext)
                    results.add(partialRatio)
                }
            }
        }
    }

    private def bindParamsToSpec() {
        TwoDimSymConfSpec spec = new TwoDimSymConfSpec()
        bindData(spec, params, [include: ['latticeSize', 'alphabetSize']])
        spec
    }

    def runSimulation() {
        TwoDimSymCARunSpec spec = new TwoDimSymCARunSpec()
        bindData(spec, params, [include: ['latticeSize', 'symmetryShiftX', "symmetryShiftY", "runTime"]])

        spec.validate()

        if (spec.symmetryShiftX == 0 && spec.symmetryShiftY == 0) {
            def message = "Both coordinates of the symmetry shift (x,y) cannot be 0."
            spec.errors.rejectValue("symmetryShiftX", null, message)
        }

        if (spec.hasErrors()) {
            def errorsJSON = spec.errors as JSON
            render errorsJSON
            return
        }

        // 2D CA (network)
        def sizes = new ArrayList<Integer>()
        sizes.add(spec.latticeSize)
        sizes.add(spec.latticeSize)

        Topology spatialTopology = new SpatialTopology()
        spatialTopology.setItsOwnNeighor(true)
        spatialTopology.setTorusFlag(true)
        spatialTopology.setMetricsType(MetricsType.Max)
        spatialTopology.setRadius(1)
        spatialTopology.setSizes(sizes)

        // this is needed just to determine the neighborhood size
        def initTopology = topologyFactory.apply(spatialTopology)
        def neighborhoodSize = initTopology.allNodes.get(0).getInNeighbors().size()

        def transitionTable = functionFactory.createRandomBoolTransitionTable(neighborhoodSize)

        NetworkFunction<Boolean> networkFunction = new NetworkFunction<Boolean>()
        networkFunction.setFunction(transitionTable)
        networkFunction.setMultiComponentUpdaterType(MultiStateUpdateType.Sync)

        Network<Integer> network = new Network<Integer>()
        network.setTopology(spatialTopology)
        network.setFunction(networkFunction)

        int[] genVector = [spec.symmetryShiftX, spec.symmetryShiftY]
        boolean[] states = NDimShiftSymmetricConfiguration.generate(genVector, spec.latticeSize)

        def actionSeries = new NetworkActionSeries<Boolean>()

        def initAction = new NetworkAction<Boolean>()
        initAction.setStartTime(0)
        initAction.setTimeLength(0d)
        initAction.setAlternationType(StateAlternationType.Replacement)
        initAction.setStates(states.toList())
        actionSeries.addAction(initAction)

//        def simConfig = new NetworkSimulationConfig()
//        simConfig.setFixedPointDetectionPeriodicity(100)
//        simConfig.setFixedPointDetectionPrecision(100)

        NetworkRunTask networkRunTask = new NetworkRunTask()
        networkRunTask.setRunTime(spec.runTime)
        networkRunTask.setNetwork(network)
        networkRunTask.setActionSeries(actionSeries)
//        networkRunTask.setSimulationConfig(simConfig)

        def runTraces = networkService.runSimulation(networkRunTask)
        def runTrace = ObjectUtil.getFirst(runTraces)

        def timeRunTrace = runTrace.transpose()

        def firstNode = timeRunTrace.components().get(0)
        def zoom = 2
        if (params.format == "svg") zoom = 1

        def spatialNetworkRunPicGenerator = (firstNode.hasLocation()) ?
                (firstNode.location.size() == 1) ?
                        JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean1D(timeRunTrace.components().size(), zoom)
                        :
                        JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean2D(zoom)
                : JavaNetworkRunSpatialPicGenerator.createNonSpatialJavaBoolean2D(zoom)

        def data = spatialNetworkRunPicGenerator.generateImageData(timeRunTrace, params.format, true)
        def data2 = new NetworkRunSpatialImageData()
        data2.format = data.format
        data2.width = data.width
        data2.height = data.height
        data2.images = data.images

        JSON.use("deep") {
            def dataJSON = data2 as JSON
            render dataJSON
        }
    }

    private def BigInteger binomial(int N, int K) {
        BigInteger ret = BigInteger.ONE;
        for (int k = 0; k < K; k++) {
            ret = ret.multiply(BigInteger.valueOf(N - k)).divide(BigInteger.valueOf(k + 1));
        }
        ret
    }

    private def createRandomBooleanFunction() {
        return new Function<Boolean, Boolean>() {

            @Override
            Integer getArity() {
                return null;
            }

            @Override
            Set<Integer> getReferencedVariableIndeces() {
                return null
            }

            @Override
            Set<String> getReferencedVariables() {
                return null
            }

            @Override
            Long getKey() {
                return null
            }

            @Override
            void setKey(Long aLong) {

            }
        }
    }
}