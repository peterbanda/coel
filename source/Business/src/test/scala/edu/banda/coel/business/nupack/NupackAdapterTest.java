package edu.banda.coel.business.nupack;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import edu.banda.coel.business.nupack.NupackAdapter;
import edu.banda.coel.business.nupack.NupackSetting;
import edu.banda.coel.business.nupack.PfuncResult;
import edu.banda.coel.business.nupack.NupackAdapter.Material;

/**
 * @author © Peter Banda
 * @since 2012
 */
@Ignore
public class NupackAdapterTest extends TestCase {

	private NupackAdapter nupackAdapter = new NupackAdapter();
	
	private static final String DNA_STRAND_PFUNC_1 = "GGGCUGUUUUUCUCGCUGACUUUCAGCCCCAAACAAAAAAUGUCAGCA";
	private static final String[] DNA_STRANDS_PFUNC_2 = new String[] {
		"AGTCTAGGATTCGGCGTGGGTTAA",
		"TTAACCCACGCCGAATCCTAGACTCAAAGTAGTCTAGGATTCGGCGTG",
		"AGTCTAGGATTCGGCGTGGGTTAACACGCCGAATCCTAGACTACTTTG"
	};
	private static final Integer[] ORDERED_COMPLEX_PFUNC_2 = new Integer[] {1, 2, 2, 3};

	@Test
	public void testPfunc1() {
		NupackSetting nupackSetting = new NupackSetting();
		nupackSetting.setPseudoFlag(true);

		final PfuncResult nupackResult = nupackAdapter.pfunc(
				nupackSetting, DNA_STRAND_PFUNC_1);

		assertNotNull(nupackResult.getFreeEnergy());
		assertNotNull(nupackResult.getPartitionFunction());
		System.out.println("Free energy: " + nupackResult.getFreeEnergy());
		System.out.println("Partition function: " + nupackResult.getPartitionFunction());
	}

	@Test
	public void testPfunc2() {
		NupackSetting nupackSetting = new NupackSetting();
		nupackSetting.setMultiFlag(true);
		nupackSetting.setTemperature(23d);
		nupackSetting.setMaterial(Material.dna1998);

		final PfuncResult nupackResult = nupackAdapter.pfunc(
				nupackSetting, Arrays.asList(DNA_STRANDS_PFUNC_2), Arrays.asList(ORDERED_COMPLEX_PFUNC_2));

		assertNotNull(nupackResult.getFreeEnergy());
		assertNotNull(nupackResult.getPartitionFunction());
		System.out.println("Free energy: " + nupackResult.getFreeEnergy());
		System.out.println("Partition function: " + nupackResult.getPartitionFunction());
	}

	@Test
	public void testPairs1() {
		NupackSetting nupackSetting = new NupackSetting();
		nupackSetting.setPseudoFlag(true);

		nupackAdapter.pairs(nupackSetting, DNA_STRAND_PFUNC_1);

//		assertNotNull(nupackResult.getFreeEnergy());
//		assertNotNull(nupackResult.getPartitionFunction());
//		System.out.println("Free energy: " + nupackResult.getFreeEnergy());
//		System.out.println("Partition function: " + nupackResult.getPartitionFunction());
	}

	@Test
	public void testPairs2() {
		NupackSetting nupackSetting = new NupackSetting();
		nupackSetting.setMultiFlag(true);
		nupackSetting.setTemperature(23d);
		nupackSetting.setMaterial(Material.dna1998);

		nupackAdapter.pairs(nupackSetting, Arrays.asList(DNA_STRANDS_PFUNC_2), Arrays.asList(ORDERED_COMPLEX_PFUNC_2));

//		assertNotNull(nupackResult.getFreeEnergy());
//		assertNotNull(nupackResult.getPartitionFunction());
//		System.out.println("Free energy: " + nupackResult.getFreeEnergy());
//		System.out.println("Partition function: " + nupackResult.getPartitionFunction());
	}
}