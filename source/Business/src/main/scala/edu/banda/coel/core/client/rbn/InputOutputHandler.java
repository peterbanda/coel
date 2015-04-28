package edu.banda.coel.core.client.rbn;

import java.util.*;

import com.banda.core.domain.Point;
import com.banda.core.util.FileUtil;
import com.banda.function.business.FunctionFactory;
import com.banda.function.domain.Function;

import edu.banda.coel.CoelRuntimeException;
import edu.banda.coel.business.BooleanNetworkBO;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class InputOutputHandler {

	// The singleton  instance
	private static final InputOutputHandler instance = new InputOutputHandler();
	private static final FileUtil fileUtil = FileUtil.getInstance();
	private final FunctionFactory functionFactory = new FunctionFactory();

	public static InputOutputHandler getInstance() {
		return instance;
	}

	private InputOutputHandler() {
		// Nothing to do
	}

	public Collection<Point<Integer>> getLocations(String fileName) {
		Collection<Point<Integer>> locations = new ArrayList<Point<Integer>>();
		String input = getFileContent(fileName);

		Integer dimensionsNum = null;
		input = input.replaceAll("\n", "");
		StringTokenizer st = new StringTokenizer(input, ";");
		while (st.hasMoreElements()) {
			String coordinatesString = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(coordinatesString.trim(), " ");
			List<Integer> coordinates = new ArrayList<Integer>();
			while (st2.hasMoreElements()) {
				coordinates.add(getNextInt(st2));
			}
			if (coordinates.isEmpty() || (dimensionsNum != null && dimensionsNum.compareTo(coordinates.size()) != 0)) {
				throw new CoelRuntimeException("Location '" + coordinatesString + "' in file " + fileName + " is either empty or have inconsistent number of coordinates!");				
			}			
			locations.add(new Point<Integer>(coordinates.toArray(new Integer[0])));
		}
		return locations;
	}

	public Collection<Boolean> getStates(String fileName) {
		Collection<Boolean> states = new ArrayList<Boolean>();
		String input = getFileContent(fileName);

		input = input.replaceAll("\n", "");
		input = input.replaceAll(" ", "");
		for (char stateChar : input.toCharArray()) {
			Boolean state = null;
			switch (stateChar) {
				case '0':state = Boolean.FALSE;break;
				case '1':state = Boolean.TRUE;break;
				default: throw new CoelRuntimeException("Char '" + stateChar + "' is not a state of BN node. 0 or 1 expected.");	 
			}
			states.add(state);
		}
		return states;
	}

	public Collection<Function<Boolean, Boolean>> getFunctions(String fileName) {
		Collection<Function<Boolean, Boolean>> functions = new ArrayList<Function<Boolean, Boolean>>();
		String input = getFileContent(fileName);

		StringTokenizer st = new StringTokenizer(input, "\n");
		int line = 1;
		while (st.hasMoreElements()) {
			String transitionTableOutputsString = st.nextToken().trim();
			Collection<Boolean> transitionTableOutputs = new ArrayList<Boolean>();
			for (char tableOutput : transitionTableOutputsString.toCharArray()) {
				Boolean output = null;
				if (tableOutput == '0') {
					output = Boolean.FALSE;
				} else if (tableOutput == '1') {
					output = Boolean.TRUE;
				} else {
					throw new CoelRuntimeException("Transition table output '" + tableOutput + "' at line " + line + " not valid. 0 or 1 expected.");
				}
				transitionTableOutputs.add(output);
			}
			functions.add(functionFactory.createBoolTransitionTable(transitionTableOutputs));
			line++;
		}
		return functions;
	}

	// Adjacency matrix supports multiple connections between same nodes
	// In that case associated matrix element holds the number of multiple connections (> 1)
	public Collection<Byte[]> getAdjacencyMatrix(String fileName, int nodesNum) {
		String input = getFileContent(fileName);

		StringTokenizer st = new StringTokenizer(input, "\n");
		// Creating adjacency matrix
		List<Byte[]> matrix = new ArrayList<Byte[]>();
		for (int i = 0; i < nodesNum; i++) {
			Byte[] adjacencyVector = new Byte[nodesNum];
			for (int j = 0; j < nodesNum; j++) {
				adjacencyVector[j] = new Byte((byte) 0);
			}
			matrix.add(adjacencyVector);
		}
		int line = 1;
		while (st.hasMoreTokens()) {
			String edge = st.nextToken();

			StringTokenizer st2 = new StringTokenizer(edge, " ");
			checkIfEmpty(st2, "Edge " + edge + " at line " + line + " is not valid!");
			int from = getNextInt(st2);

			checkIfEmpty(st2, "Edge " + edge + " at line " + line + " is not valid!");
			int to = getNextInt(st2);
			
			matrix.get(to)[from]++;    // = Boolean.TRUE
			line++;
		}
		return matrix;
	}

	public void storeAdjacencyMatrix(BooleanNetworkBO bnBO) {
		StringBuffer sb = new StringBuffer();

		int toNode = 0;
		for (Boolean[] adjacencyVector : bnBO.getAllNodeNeighborhoodFlags()) {
			int fromNode = 0;
			for (Boolean adjacentFlag : adjacencyVector) {
	            if (adjacentFlag) {
	            	sb.append(fromNode);
	            	sb.append(" ");
	            	sb.append(toNode);
	            	sb.append("\n");	            
	            }
				fromNode++;
			}
			toNode++;
		}
		String currentTime = getFileUtility().getDateAsString(new Date());
		storeToFile(sb.toString(), "net_" + currentTime + ".net");
	}

	public void storeFunctions(BooleanNetworkBO bnBO) {
		StringBuffer sb = new StringBuffer();

		for (Boolean[] functionOutputs : bnBO.getFunctionsOutputs()) {
			for (Boolean outuput : functionOutputs) {
				sb.append(outuput ? '1' : '0');
			}
			sb.append("\n");
		}
		String currentTime = getFileUtility().getDateAsString(new Date());
		storeToFile(sb.toString(), "net_" + currentTime + ".fn");
	}

	public void storeStates(BooleanNetworkBO bnBO) {
		StringBuffer sb = new StringBuffer();

		for (Boolean state : bnBO.getNodesStates()) {
			sb.append(state ? '1' : '0');
		}
		String currentTime = getFileUtility().getDateAsString(new Date());
		storeToFile(sb.toString(), "net_" + currentTime + ".st");
	}

	private static int getNextInt(StringTokenizer st) {
		String token = null;
		int num;
		try {
			token = st.nextToken();
			num = Integer.parseInt(token);		
		} catch (NumberFormatException e) {
			throw new CoelRuntimeException("String '" + token + "' is not valid Integer!");
		}
		return num;
	}

	private static void checkIfEmpty(StringTokenizer st, String errorMessage) {
		if (!st.hasMoreElements()) {
			throw new CoelRuntimeException(errorMessage);
		}
	}

	private static String getFileContent(String fileName) {
		return fileUtil.readStringFromFileSafe(fileName);
	}

	private static void storeToFile(String fileName, String content) {
		fileUtil.overwriteStringToFileSafe(content, fileName);
	}

	private static FileUtil getFileUtility() {
		return FileUtil.getInstance();
	}
}
