package edu.banda.coel.business.nupack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.banda.core.exec.ExternalProcess;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.banda.core.util.FileUtil;
import com.banda.core.util.ParseUtil;
import com.banda.core.util.RandomUtil;

import edu.banda.coel.CoelRuntimeException;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class NupackAdapter {

	private static final String NUPACK_HOME_ENV_VARIABLE = "NUPACKHOME";
	private static final String DEFAULT_FOLDER = "${" + NUPACK_HOME_ENV_VARIABLE + "}" + File.separator + "bin";
	private static final String INFO_LINE_START = "%";

	private final Log log = LogFactory.getLog(getClass());

	public enum Command {
		pfunc,
		pairs
	}

	public enum Extension {
		in,
		ppairs,
		epairs;

		@Override
		public String toString() {
			return "." + super.toString();
		}
	}

	private static interface Argument {
		final String MULTI = "-multi";
		final String PSEUDO = "-pseudo";
		final String MATERIAL = "-material";
		final String DANGLES = "-dangles";
		final String TEMPERATURE = "-T";
		final String CUTOFF = "-cutoff";
	}

	public enum Material {
		rna1995, dna1998, rna1999
	}
	
	public enum Dangles {
		none, some, all
	}

	private final FileUtil fileUtil = FileUtil.getInstance();

	public PfuncResult pfunc(
		NupackSetting setting,
		String strand
	) throws NupackHomeNotSetException {

		checkNupackHome();
		final Collection<String> args = buildArgs(setting);
		final String tempInputFilePrefix = createTempInputFile(strand);
		args.add(tempInputFilePrefix);

		try {
			final String result = executeCommand(Command.pfunc, args);
			return createPfuncResult(result);
		} finally {
			fileUtil.removeFile(tempInputFilePrefix + Extension.in);			
		}
	}

	public PfuncResult pfunc(
		NupackSetting setting,
		List<String> strands,
		List<Integer> orderedComplex
	) throws NupackHomeNotSetException {

		checkNupackHome();
		final Collection<String> args = buildArgs(setting);
		final String tempInputFilePrefix = createTempInputFile(strands, orderedComplex);
		args.add(tempInputFilePrefix);

		try {
			final String result = executeCommand(Command.pfunc, args);
			return createPfuncResult(result);
		} finally {
			fileUtil.removeFile(tempInputFilePrefix + Extension.in);			
		}
	}

	public void pairs(
		NupackSetting setting,
		String strand
	) throws NupackHomeNotSetException {

		checkNupackHome();
		final Collection<String> args = buildArgs(setting);
		final String tempInputFilePrefix = createTempInputFile(strand);
		args.add(tempInputFilePrefix);

		try {
			executeCommand(Command.pairs, args);
			createPpairsResult(tempInputFilePrefix, setting.isPseudoFlag());
//			return createPfuncResult(result);
		} finally {
			fileUtil.removeFile(tempInputFilePrefix + Extension.in);			
		}
	}

	public void pairs(
		NupackSetting setting,
		List<String> strands,
		List<Integer> orderedComplex
	) throws NupackHomeNotSetException {

		checkNupackHome();
		final Collection<String> args = buildArgs(setting);
		final String tempInputFilePrefix = createTempInputFile(strands, orderedComplex);
		args.add(tempInputFilePrefix);

		try {
			executeCommand(Command.pairs, args);
			PpairsResult ppairsResult = createPpairsResult(tempInputFilePrefix, setting.isPseudoFlag());
			PpairsResult epairsResult = handleEpairsOutputFile(tempInputFilePrefix);
//			for (String line : StringUtils.split(ppairs, '\n')) {
//				System.out.println(line);
//			}
//			System.out.println(result);
//			return createPfuncResult(result);
		} finally {
			fileUtil.removeFile(tempInputFilePrefix + Extension.in);
		}
	}

	private PpairsResult createPpairsResult(String ppairsFilePrefix, boolean pseudoFlag) {
		PpairsResult result = new PpairsResult();
		try {
			String ppairs = fileUtil.readStringFromFileSafe(ppairsFilePrefix + Extension.ppairs);
			String[] lines = StringUtils.split(ppairs, '\n');
			for (String line : lines) {
				if (line.startsWith(INFO_LINE_START)) {
					log.debug(line);
				} else {
					if (!result.hasBasesNumber()) {
						final int numberOfBases = ParseUtil.parseInt(line, "total number of bases"); 
						result.setBasesNumber(numberOfBases);
					} else {
						BasePairProbabilityEntry entry = null;
						// parse the line
						String[] probElements = StringUtils.split(line);
						final int baseIndex1 = ParseUtil.parseInt(probElements[0], "base index");
						final int baseIndex2 = ParseUtil.parseInt(probElements[1], "base index");
						final double pairProbability = ParseUtil.parseDouble(probElements[2], "pair probability");
						if (pseudoFlag) {
							final double nestedPairProbability = ParseUtil.parseDouble(probElements[3], "nested pair probability");
							final double nonnestedPairProbability = ParseUtil.parseDouble(probElements[4], "nonnested pair probability");
							entry = new BasePairProbabilityEntry(baseIndex1, baseIndex2, pairProbability, nestedPairProbability, nonnestedPairProbability);
						} else {
							entry = new BasePairProbabilityEntry(baseIndex1, baseIndex2, pairProbability);
						}
						result.addPairProbabilityEntry(entry);
					}
				}
			}
		} finally {
			fileUtil.removeFile(ppairsFilePrefix + Extension.ppairs);
		}
		return result;
	}

	private PpairsResult handleEpairsOutputFile(String epairsFilePrefix) {
		PpairsResult result = new PpairsResult();
		try {
			String ppairs = fileUtil.readStringFromFileSafe(epairsFilePrefix + Extension.epairs);
			String[] lines = StringUtils.split(ppairs, '\n');
			for (String line : lines) {
				if (line.startsWith(INFO_LINE_START)) {
					log.debug(line);
				} else {
					if (!result.hasBasesNumber()) {
						final int numberOfDistinctBases = ParseUtil.parseInt(line, "total number of distinct bases"); 
						result.setDistinctBasesNumber(numberOfDistinctBases);
					} else {
						// parse the line
						String[] probElements = StringUtils.split(line);
						final int baseIndex1 = ParseUtil.parseInt(probElements[0], "base index");
						final int baseIndex2 = ParseUtil.parseInt(probElements[1], "base index");
						final double pairNumber = ParseUtil.parseDouble(probElements[2], "pair number");
						DistinctBasePairNumberEntry entry = new DistinctBasePairNumberEntry(baseIndex1, baseIndex2, pairNumber);
						result.addDistinctBasePairNumberEntry(entry);
					}
				}
			}
		} finally {
			fileUtil.removeFile(epairsFilePrefix + Extension.epairs);
		}
		return result;
	}

	private PfuncResult createPfuncResult(String result) {
		final String[] lines = StringUtils.split(result, '\n');
		List<String> filteredLines = new ArrayList<String>();
		for (String line : lines) {
			if (line.startsWith(INFO_LINE_START)) {
				log.debug(line);
			} else {
				filteredLines.add(line);
			}
		}
		if (filteredLines.size() != 2) {
			throw new CoelRuntimeException("Expected number of output lines (except info/comments) for pfunc NUPACK function is 2, but got " + filteredLines.size());
		}

		final Double freeEnergy = ParseUtil.parseDouble(filteredLines.get(0), "free energy"); 
		final Double partitionFunction = ParseUtil.parseDouble(filteredLines.get(1), "partition function");

		return new PfuncResult(freeEnergy, partitionFunction);
	}

	private Collection<String> buildArgs(NupackSetting setting) {
		Collection<String> args = new ArrayList<String>();
		if (setting.getTemperature() != null) {
			args.add(Argument.TEMPERATURE);
			args.add(setting.getTemperature().toString());
		}
		if (setting.isMultiFlag()) {
			args.add(Argument.MULTI);
		}
		if (setting.isPseudoFlag()) {
			args.add(Argument.PSEUDO);
		}
		if (setting.getMaterial() != null) {
			args.add(Argument.MATERIAL);
			args.add(setting.getMaterial().toString());
		}
		if (setting.getDangles() != null) {
			args.add(Argument.DANGLES);
			args.add(setting.getDangles().toString());
		}
		return args;
	}

	private String executeCommand(Command command, Collection<String> args) {
		final ExternalProcess process = new ExternalProcess(false);
		return process.execute(DEFAULT_FOLDER + File.separator + command, args);
	}

	private String createTempInputFile(String strand) { 
		String fileName = RandomUtil.nextReadableString(10);
		fileUtil.overwriteStringToFileSafe(strand, fileName + Extension.in);
		return fileName;
	}

	private String createTempInputFile(List<String> strands, List<Integer> orderedComplex) {
		StringBuilder sb = new StringBuilder();
		sb.append(strands.size());
		sb.append('\n');
		for (String strand : strands) {
			sb.append(strand);
			sb.append('\n');
		}
		sb.append(StringUtils.join(orderedComplex, ' '));
		String fileName = RandomUtil.nextReadableString(10);
		fileUtil.overwriteStringToFileSafe(sb.toString(), fileName + Extension.in);
		return fileName;
	}

	private void checkNupackHome() {
		final String nupackHome = System.getenv(NUPACK_HOME_ENV_VARIABLE);
		if (nupackHome == null) {
			throw new NupackHomeNotSetException();
		}
	}
}