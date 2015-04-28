package edu.banda.coel.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.banda.core.domain.ComponentRunTrace;
import com.banda.core.domain.TimeRunTrace;
import com.banda.core.dynamics.StateAlternationType;
import com.banda.core.util.FileUtil;
import com.banda.core.util.ObjectUtil;
import com.banda.core.util.RandomUtil;
import com.banda.math.domain.rand.DiscreteDistribution;
import com.banda.math.domain.rand.RandomDistribution;
import com.banda.network.domain.NetworkAction;
import com.banda.network.domain.NetworkActionSeries;
import com.banda.network.domain.NetworkSimulationConfig;
import com.banda.network.domain.TopologicalNode;

import edu.banda.coel.business.netpic.JavaNetworkRunSpatialPicGenerator;
import edu.banda.coel.business.netpic.NetworkRunSpatialPicGenerator;
import edu.banda.coel.domain.service.NetworkService;
import edu.banda.coel.server.CoelTest;
import edu.banda.coel.task.network.NetworkPerformanceEvaluateTask;
import edu.banda.coel.task.network.NetworkRunTask;
import edu.banda.coel.task.network.SpatialNetworkPerformanceEvaluateTask;

/**
 * @author Peter Banda
 * @since 2014
 */
@Transactional
public class NetworkServiceTest extends CoelTest {

	@Autowired
	NetworkService networkService;

	@Test
	public <T> void testRunBooleanDerridaAnalysis() {
		NetworkRunTask<Boolean> taskDef = new NetworkRunTask<Boolean>();

		taskDef.setNetworkId(1013l);
		taskDef.setRunTime(1);
		taskDef.setRepetitions(10);

		// sim config
		NetworkSimulationConfig simConfig = new NetworkSimulationConfig();
		simConfig.setFixedPointDetectionPeriodicity(25d);
		taskDef.setSimulationConfig(simConfig);

		networkService.runBooleanDerridaAnalysis(taskDef);
	}

	@Test
	@Ignore
	public <T> void testRunNetwork() {
		NetworkRunTask<T> networkRunTask = new NetworkRunTask<T>();
		networkRunTask.setRunTime(100);
		networkRunTask.setNetworkId(1001l);

		NetworkActionSeries<T> actionSeries = new NetworkActionSeries<T>();
		
		NetworkAction<T> initNetworkAction = new NetworkAction<T>();
		initNetworkAction.setStartTime(0);
		initNetworkAction.setTimeLength(0d);
		initNetworkAction.setAlternationType(StateAlternationType.Replacement);
		initNetworkAction.setStateDistribution((RandomDistribution<T>) new DiscreteDistribution<Boolean>(new double[]{0.5, 0.5}, new Boolean[]{true, false}));
		actionSeries.addAction(initNetworkAction);

		networkRunTask.setActionSeries(actionSeries);

		Collection<ComponentRunTrace<T, TopologicalNode>> runTraces = networkService.runSimulation(networkRunTask);
		assertNotEmpty(runTraces);
	}

//	@Test
	public <T> void testRunPerformanceEvaluation() {
		NetworkPerformanceEvaluateTask<T> taskDef = new NetworkPerformanceEvaluateTask<T>();

		taskDef.setNetworkId(1014l);
		taskDef.setNetworkActionSeriesIds(Collections.singleton(1000l));
		taskDef.setNetworkEvaluationIds(Collections.singleton(1000l));
		taskDef.setRunTime(300);
		taskDef.setRepetitions(10);

		// sim config
		NetworkSimulationConfig simConfig = new NetworkSimulationConfig();
		simConfig.setFixedPointDetectionPeriodicity(25d);
		taskDef.setSimulationConfig(simConfig);

		networkService.runPerformanceEvaluation(taskDef);
	}

//	@Test
	public <T> void testRunSpatialPerformanceEvaluation() {
		SpatialNetworkPerformanceEvaluateTask<T> taskDef = new SpatialNetworkPerformanceEvaluateTask<T>();

		taskDef.setNetworkId(1014l);
		taskDef.setNetworkActionSeriesIds(Collections.singleton(1000l));
		taskDef.setNetworkEvaluationIds(Collections.singleton(1000l));
		taskDef.setRunTime(300);
		taskDef.setRepetitions(10);
		taskDef.setSizeFrom(19);
		taskDef.setSizeTo(21);

		// sim config
		NetworkSimulationConfig simConfig = new NetworkSimulationConfig();
		simConfig.setFixedPointDetectionPeriodicity(25d);
		taskDef.setSimulationConfig(simConfig);

		networkService.runSpatialPerformanceEvaluation(taskDef);
	}

//	@Test
	public <T> void testRunNetworkWithPics() {
		NetworkRunTask<T> networkRunTask = new NetworkRunTask<T>();
//		networkRunTask.setRunTime(296);
//		networkRunTask.setNetworkId(1029l);
		networkRunTask.setRunTime(100);
		networkRunTask.setNetworkId(1013l);

		NetworkActionSeries<T> actionSeries = new NetworkActionSeries<T>();

//		List<Boolean> initialState = new ArrayList<Boolean>();
//		for (int i = 0; i < 37; i++) {
//			initialState.add(RandomUtil.nextBoolean());
//		}
//		List<Boolean> initialState = createSymmetricState(40, 3, 5);

		NetworkAction<T> initNetworkAction = new NetworkAction<T>();
		initNetworkAction.setStartTime(0);
		initNetworkAction.setTimeLength(0d);
		initNetworkAction.setAlternationType(StateAlternationType.Replacement);
		initNetworkAction.setStateDistribution((RandomDistribution<T>) new DiscreteDistribution<Boolean>(new double[]{0.005, 0.995}, new Boolean[]{true, false}));
//		initNetworkAction.setStateDistribution((RandomDistribution<T>) new RepeatedDistribution<Boolean>(initialState.toArray(new Boolean[0])));
//		initNetworkAction.setStates((List<T>) initialState);
		actionSeries.addAction(initNetworkAction);

		networkRunTask.setActionSeries(actionSeries);

		Collection<ComponentRunTrace<T, TopologicalNode>> runTraces = networkService.runSimulation(networkRunTask);
		ComponentRunTrace<T, TopologicalNode> runTrace = ObjectUtil.getFirst(runTraces);
		TimeRunTrace<T, TopologicalNode> timeRunTrace = runTrace.transpose();
		TopologicalNode firstNode = timeRunTrace.components().get(0);

		int zoom = 1;
		final NetworkRunSpatialPicGenerator<Boolean> spatialNetworkRunPicGenerator =
			(firstNode.hasLocation()) ? 
				(firstNode.getLocation().size() == 1) ?
					JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean1D(networkRunTask.getRunTime(), zoom)
				:
					JavaNetworkRunSpatialPicGenerator.createSpatialJavaBoolean2D(zoom)
			: JavaNetworkRunSpatialPicGenerator.createNonSpatialJavaBoolean2D(zoom);

		List<String> data = spatialNetworkRunPicGenerator.generateSVGStrings((TimeRunTrace<Boolean, TopologicalNode>) timeRunTrace);
		int i = 0;
		for (String pic : data) {
			FileUtil.getInstance().overwriteStringToFileSafe(pic, "ca" + i + ".svg");
			i++;
		}
		assertNotEmpty(runTraces);
	}

	private List<Boolean> createSymmetricState(int size, int vx, int vy) {
		boolean[][] matrix = new boolean[size][size];
		for (int xstart = 0; xstart < size; xstart++)
			for (int ystart = 0; ystart < size; ystart++) {
				int x = xstart;
				int y = ystart;
				boolean value = RandomUtil.nextBoolean();
				do {
					matrix[x][y] = value;
					x += vx;
					y += vy;
					x = x % size;
					y = y % size;
				} while (x != xstart || y != ystart);
			}
		List<Boolean> state = new ArrayList<Boolean>();
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				state.add(matrix[x][y]);
		return state;
	}
}