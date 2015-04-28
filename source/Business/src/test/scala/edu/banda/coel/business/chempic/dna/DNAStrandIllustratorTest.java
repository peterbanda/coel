package edu.banda.coel.business.chempic.dna;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.batik.svggen.SVGGraphics2D;
import org.junit.Test;

import com.banda.core.util.FileUtil;

import edu.banda.coel.business.chempic.ChemistryPicGeneratorImpl;
import edu.banda.coel.core.svg.SVGUtil;
import edu.banda.coel.domain.service.ChemistryPicGenerator;

/**
 * @author © Peter Banda
 * @since 2013
 */
public class DNAStrandIllustratorTest extends TestCase {

	private final ChemistryPicGenerator chemPicGenerator = new ChemistryPicGeneratorImpl(false);
	private final FileUtil fileUtil = FileUtil.getInstance();

	@Test
	public void testDrawDnaStrand() {		
		try {
			final String output = chemPicGenerator.createDNAStrandSVG("[a]:[b e f^]::[c d e^* alla rt^]");
			fileUtil.overwriteStringToFile(output, "dnaSpecies.svg");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDrawDnaStrandReaction() {
		try {
			String[] reactants = new String[]{"{ls1}<us1>[ds]<us2>{ls2}", "{ls1}<us1>[ds]"};
			String[] products = new String[]{"{ls1}", "[aa]::{us1 a b}[ds]{us1 c^* d}::[a]"};

			final String output = chemPicGenerator.createDNAReactionSVG(Arrays.asList(reactants), Arrays.asList(products), true);
			fileUtil.overwriteStringToFile(output, "dnaReaction.svg");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testDrawDnaStrandReaction2() {		
		try {
			String[] reactants = new String[]{"<4 1^ 2 3^>", "{1^* 4 6^}[2 3^ 5^ 6 7^]<12 9^ 5 4 8>"};
			String[] products = new String[]{"<4>[1^ 2 3^]:{5}[6 7^]<12 9^>", "<2 3^ 5^>"};

			final String output = chemPicGenerator.createDNAReactionSVG(Arrays.asList(reactants), Arrays.asList(products), false);
			fileUtil.overwriteStringToFile(output, "dnaReaction2.svg");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}