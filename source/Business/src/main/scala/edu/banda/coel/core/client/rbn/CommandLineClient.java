package edu.banda.coel.core.client.rbn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.banda.core.domain.Point;
import com.banda.core.util.FileUtil;
import com.banda.core.util.ParseUtil;
import com.banda.function.business.FunctionEvaluatorFactoryImpl;
import com.banda.function.domain.Function;
import com.banda.function.enumerator.ListEnumeratorFactoryImpl;
import com.banda.function.evaluator.FunctionEvaluatorFactory;
import com.banda.core.parallel.Parallelizer;
import com.banda.core.parallel.RunnableWith;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.business.BooleanNetworkBO;
import edu.banda.coel.business.SelfOrganizedMapBO;
import edu.banda.coel.core.client.gui.BNFrame;
import edu.banda.coel.core.domain.BooleanNetworkFactory;
import edu.banda.coel.core.domain.som.SelfOrganizedMapFactory;
import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.som.SelfOrganizedMap;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class CommandLineClient {

	public enum LearningType
	{
		Final, MinimumDistance;
		
		public static LearningType fromOrdinal(int i) {
			for (LearningType learningType : values()) {
				if (learningType.ordinal() == i) {
					return learningType;
				}
			}
			return null;
		}
	}

	public enum OutputStyle
	{
		JustTotalDistance, Coordinates, GraphPics;
		
		public static OutputStyle fromOrdinal(int i) {
			for (OutputStyle outputStyle : values()) {
				if (outputStyle.ordinal() == i) {
					return outputStyle;
				}
			}
			return null;
		}
	}

	// DEFAULTS
	private static final double DEFAULT_TUNING_PHASE_START = 0.5;
	private static final LearningType DEFAULT_LEARNING_TYPE = LearningType.Final;
	private static final OutputStyle DEFAULT_OUTPUT_STYLE = OutputStyle.JustTotalDistance;
	private static final int DEFAULT_REPETITIONS_NUM = 1;

	private Integer nodesNum;
	private Integer connectionsPerNode;
	private Integer[] dimensionSizes;
	private Integer repetitionsNum = DEFAULT_REPETITIONS_NUM;
	private String connectionsInFile;
	private String functionsInFile;
	private String locationsInFile;
	private String statesInFile;
	private boolean outputToFileFlag = false;
	private String outputFileName;
	private Double tuningPhaseStart = DEFAULT_TUNING_PHASE_START;
	private LearningType learningType = DEFAULT_LEARNING_TYPE;
	private OutputStyle outputStyle = DEFAULT_OUTPUT_STYLE;
	private boolean showManualFlag = false;
	private boolean runGuiClient = false;
	private Integer threadsNum = 1;

    private final FunctionEvaluatorFactory functionEvaluatorFactory = new FunctionEvaluatorFactoryImpl(
    		new ListEnumeratorFactoryImpl());

	// Input passed by command line
	private String[] args;

	public CommandLineClient(String[] args) {
		this.args = args;
	}

	private void manual() {
		println("");
		println("RBN Playground 0.1 - (C) 2013 Peter Banda / TeuscherLab");
		println("http://www.peterbanda.net");
		println("");
		println("use: java -jar coel-consoleclient.jar [options]");
		println("");
		println("---------------------------------------------------------");
		println("");
		println("Common Options");
		println("");
		println("-n       : # of nodes");
		println("-k       : random network will be generated with given # of connections per node");
		println("-d       : dimension sizes (specified as space delimited list) (default 2D - square / rectangle)");
		println("-r       : # of repetitions (default 1)");
		println("-i1      : name of input file to load connections from");
		println("-i2      : name of input file to load functions from");
		println("-i3      : name of input file to load node coordinates from");
		println("-i4      : name of input file to load node states from");
		println("-o       : name of file to write output to (if blank, name will be generated)");
		println("");
		println("SOM Learning Options");
		println("");
		println("-t       : relative begining of the tuning phase (default 0.5) - not suppored yet");
		println("-l       : learning type: 0 - final (default)");
		println("                          1 - min distance ");
		println("-s       : output style: 0 - just the total distance (default)");
		println("                         1 - type 0 with node coordinates");
		println("                         2 - type 1 with additional network picture(s) - not supported yet");
		println("");
		println("Other");
		println("");
		println("-h       : prints this manual (help)");		
		println("-u       : runs GUI client");
		println("-m       : number of threads to use (default 1 - sequential execution)");		
		println("           considered only if number of repetitions > 1");		
	}

	private boolean isOption(int argIndex) {
		String s = args[argIndex];
		return s != null && s.startsWith("-");
	}

	private char getOption(int argIndex) {
		String s = args[argIndex];
		String arg = s.substring(1, s.length());
		return Character.toLowerCase(arg.charAt(0));
	}

	private boolean isValue(int argIndex) {
		return !isOption(argIndex);
	}

	private String getValueSafe(int argIndex) {
		if (argIndex >= args.length) {
			throw new CoelRuntimeException("No value found after " + args[argIndex - 1] + " option.");
		}
		return isValue(argIndex) ? args[argIndex] : null;
	}

	private int getIntSafe(int argIndex, String variableName) {
		return ParseUtil.parseInt(getValueSafe(argIndex), variableName);
	}

	private double getDoubleSafe(int argIndex, String variableName) {
		return ParseUtil.parseDouble(getValueSafe(argIndex), variableName);
	}

	private void readOptions() {
		if (args == null || args.length == 0) {
			manual();
			return;
		}
		for (int i = 0; i < args.length; i++) {
			if (isOption(i)) {     // it looks like option
				switch (getOption(i)) {
					case 'n':
						i++;
						nodesNum = getIntSafe(i, "number of nodes (-n)");
						break;
					case 'k':
						i++;
						connectionsPerNode = getIntSafe(i, "connections per node (-k)");
						break;
					case 'd':
						List<Integer> dimensionSizesList = new ArrayList<Integer>();
						i++;
						do {
							dimensionSizesList.add(getIntSafe(i, "dimension size"));
							i++;
						} while (i < args.length && isValue(i));
						i--;
						dimensionSizes = dimensionSizesList.toArray(new Integer[0]);
						break;
					case 'r':
						i++;
						repetitionsNum = getIntSafe(i, "number of repetitions (-r)");
						break;
					case 'i':
						if (args[i].length() != 3) {
							throw new CoelRuntimeException("Option " + args[i] + " not recognized. You might want to try -i1, -i2 or -i3...");
						}
						Character inOption = args[i].charAt(2); 
						i++;
						switch (inOption) {
							case '1':
								connectionsInFile = getValueSafe(i);
								break;
							case '2':
								functionsInFile = getValueSafe(i);
								break;								
							case '3':
								locationsInFile = getValueSafe(i);
								break;
							case '4':
								statesInFile = getValueSafe(i);
								break;
							default:
								throw new CoelRuntimeException("Option " + args[i - 1] + " not recognized. You might want to try -i1, -i2 or -i3...");
						}
					case 'o':
						outputToFileFlag = true;
						if (i < args.length - 1) {
							outputFileName = getValueSafe(i + 1);
							if (outputFileName != null) {
								i++;
							}
						}
						break;
					case 't':
						i++;
						tuningPhaseStart = getDoubleSafe(i, "tuning phase start (-t)");
						break;
					case 'l':
						i++;
						learningType = LearningType.fromOrdinal(getIntSafe(i, "learning type (-l)"));
						break;
					case 's':
						i++;
						outputStyle = OutputStyle.fromOrdinal(getIntSafe(i, "output style (-s)"));
						break;
					case 'h':
						showManualFlag = true;
						break;
					case 'u':
						runGuiClient = true;
						break;
					case 'm':
						i++;
						threadsNum = getIntSafe(i, "number of threads (-m)");
						break;
					default:
						throw new CoelRuntimeException("Option " + args[i] + " not recognized.");
				}
			} else {
				throw new CoelRuntimeException("Input '" + args[i] + "' is not valid. Option must start with \'-\'.");
			}
		}
	}

	private void validate() {
		if (showManualFlag || runGuiClient) {
			return;
		}
		if (nodesNum == null) {
			throw new CoelRuntimeException("The -n option expected!");
		}
		if (nodesNum < 1) {
			throw new CoelRuntimeException("The number of nodes (-n) must be positive Integer!");
		}		
		if (repetitionsNum < 1) {
			throw new CoelRuntimeException("The number of repetitions (-r) must be positive Integer!");
		}		
		if (threadsNum < 1) {
			throw new CoelRuntimeException("The number of threads (-m) must be positive Integer!");
		}		
		if (dimensionSizes != null) {
			Integer spaceSize = 1;
			for (Integer dimensionSize : dimensionSizes) {
				if (dimensionSize < 1) {
					throw new CoelRuntimeException("The dimension size (-d) must be the list of positive Integers!");
				}
				spaceSize *= dimensionSize;
			}
			if (!spaceSize.equals(nodesNum)) {
				throw new CoelRuntimeException("The total space size " + spaceSize + " is not equal to the number of nodes " + nodesNum);
			}
		}
		if (connectionsInFile == null && connectionsPerNode == null) {
			throw new CoelRuntimeException("No network / graph to handle: -i1 or -k option expected!");
		}
		if (connectionsPerNode != null && connectionsPerNode > nodesNum) {
			throw new CoelRuntimeException("Number of connections '" + connectionsPerNode + "' is greater than the number of nodes '" + nodesNum + "'");
		}
		if (connectionsPerNode != null && connectionsInFile != null) {
			println("Warning:");
			println(" Since -k is specified, random network will be generated. As a matter of fact -i option is ignored.");
		}
	}

	private void execute() {
		if (runGuiClient) {
			BooleanNetworkBO bnBO = null;
			if (nodesNum != null && (connectionsPerNode != null || connectionsInFile != null)) {
				bnBO = createBooleanNetworkBO();
				if (locationsInFile == null) {
					bnBO.set2dTopology();
				}
				if (statesInFile == null) {
					bnBO.setRandomStates();
				}
				println("Boolean network created.");				
			}
			println("Running GUI client...");
			BNFrame bnFrame = BNFrame.createBNFrame(bnBO);
			return;
		}
		if (showManualFlag) {
			println("Showing manual (help). Other options are ignored...");
			manual();
			return;
		}
		if (threadsNum > 1 && repetitionsNum > 1) {
			runSomLearningInParallel();
		} else {
			runSomLearningSequentially();
		}
	}

	private String getOutputFileName() {
		if (outputToFileFlag) {
			if (outputFileName == null) {
				StringBuffer sb = new StringBuffer();
				sb.append("som_");
				if (connectionsPerNode != null) {
					if (connectionsPerNode == 0) {
						sb.append("cycle_");
					} else {
						sb.append("k_");
					}
				} else {
					sb.append("custom_");
				}
				sb.append(nodesNum);
				sb.append("_");
				sb.append(getFileUtility().getDateAsString(new Date()));
				outputFileName = sb.toString();
			}
		}
		return outputFileName;
	}

	private BooleanNetworkBO createBooleanNetworkBO() {
		BooleanNetwork bn = null;
		if (connectionsPerNode != null) {
			if (connectionsPerNode == 0) {
				bn = getBooleanNetworkFactory().create1dCycleBNWithRandomFunctions(nodesNum);
			} else {
				bn = getBooleanNetworkFactory().createRandomBNWithFixK(nodesNum, connectionsPerNode, true);
			}
		} else {
			Collection<Byte[]> adjacencyMatrix = getInputHandler().getAdjacencyMatrix(connectionsInFile, nodesNum);
			bn = getBooleanNetworkFactory().createBNWithRandomFunctions(adjacencyMatrix);
		}
		if (bn != null) {
			BooleanNetworkBO bnBO = new BooleanNetworkBO(bn, functionEvaluatorFactory);
			bnBO.assignIdentifiers();
			if (locationsInFile != null) {
				bnBO.setNodeLocations(getInputHandler().getLocations(locationsInFile));
			}
			if (functionsInFile != null) {
				Collection<Function<Boolean, Boolean>> functions = getInputHandler().getFunctions(functionsInFile);
				bnBO.setFunctions(functions);
			}
			if (statesInFile != null) {
				Collection<Boolean> states = getInputHandler().getStates(statesInFile);
				bnBO.setNodesStates(states);
			}
			return bnBO;
		}
		return null;
	}

	private void runSomLearningSequentially() {
		for (int i = 0; i < repetitionsNum; i++) {
			runSomLearningOnce();
		}
	}

	private void runSomLearningInParallel() {
		Parallelizer<Object> parallelizer = new Parallelizer<Object>(createSomLearningRunnableUnit(), threadsNum, new Object(), repetitionsNum);
		parallelizer.run();
	}

	private RunnableWith<Object> createSomLearningRunnableUnit() {
		return new RunnableWith<Object>() {

			@Override
			public void run(Object objectToProcess) {
				runSomLearningOnce();	
			}				
		};
	}

	private void runSomLearningOnce() {
		// Create BN and SOM
		BooleanNetworkBO bnBO = createBooleanNetworkBO();
		SelfOrganizedMap<Double> som = getSelfOrganizedMapFactory().createSelfOrganizedMapBOAdvanced(bnBO, new Point<Integer>(dimensionSizes));
		SelfOrganizedMapBO<Double> somBO = new SelfOrganizedMapBO<Double>(som);
		somBO.initializeWeights();

		int outputDistance = Integer.MAX_VALUE;
		List<Point<Integer>> bestNodeLocations = null;

		// SOM learning
		while (somBO.learnOneStep()) {
			if (learningType == LearningType.MinimumDistance) {
				List<Point<Integer>> locations = somBO.getDistanceBasedBmuLocations();
				bnBO.setNodeLocations(locations);
				int newDistance = bnBO.getTotalNeighborDistance();
				if (newDistance < outputDistance) {
					outputDistance = newDistance;
					bestNodeLocations = bnBO.getNodeLocations();
				}
			}
		}
		if (outputDistance == Integer.MAX_VALUE) {
			bestNodeLocations = somBO.getDistanceBasedBmuLocations();
			bnBO.setNodeLocations(bestNodeLocations);
			outputDistance = bnBO.getTotalNeighborDistance();
		}

		// Output
		if (outputStyle == OutputStyle.JustTotalDistance) {
			sendToOutput(outputDistance);				
		} else if (outputStyle == OutputStyle.Coordinates) {
			sendToOutput(outputDistance, bestNodeLocations);				
		} else {
			// TODO
		}
	}

	private void sendToOutput(int distance) {
		sendToOutput(distance, null);
	}

	private synchronized void sendToOutput(int distance, List<Point<Integer>> locations) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(distance);
			sb.append('\n');
			if (locations != null) {
				for (Point<Integer> location : locations) {
					sb.append(location.toString());
					sb.append(';');
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append('\n');
			}
			// Standard output
			System.out.print(sb.toString());
			// File output if file is set
			if (getOutputFileName() != null) {
				getFileUtility().appendStringToFile(sb.toString(), outputFileName);						
			}
		} catch (FileNotFoundException e) {
			throw new CoelRuntimeException("Output file " + outputFileName + " can not be found.", e);
		} catch (IOException e) {
			throw new CoelRuntimeException("I/O problems occured while reading a file " + outputFileName, e);
		}		
	}

	private static void println(String s) {
		System.out.println(s);
	}

	private BooleanNetworkFactory getBooleanNetworkFactory() {
		return BooleanNetworkFactory.getInstance();
	}

	private SelfOrganizedMapFactory getSelfOrganizedMapFactory() {
		return SelfOrganizedMapFactory.getInstance();
	}

	private FileUtil getFileUtility() {
		return FileUtil.getInstance();
	}

	private InputOutputHandler getInputHandler() {
		return InputOutputHandler.getInstance();	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CommandLineClient clc = new CommandLineClient(args);
		try {			
			clc.readOptions();
			clc.validate();
			clc.execute();
		} catch (CoelRuntimeException e) {
			println("Error occured:");
			println(" " + e.getMessage());
		}
	}
}