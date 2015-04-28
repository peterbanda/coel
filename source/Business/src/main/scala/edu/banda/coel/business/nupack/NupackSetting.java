package edu.banda.coel.business.nupack;

import edu.banda.coel.business.nupack.NupackAdapter.Dangles;
import edu.banda.coel.business.nupack.NupackAdapter.Material;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class NupackSetting {

	private Double temperature;
	private boolean multiFlag;
	private boolean pseudoFlag;
	private Material material;
	private Dangles dangles;

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public boolean isMultiFlag() {
		return multiFlag;
	}

	public void setMultiFlag(boolean multiFlag) {
		this.multiFlag = multiFlag;
	}

	public boolean isPseudoFlag() {
		return pseudoFlag;
	}

	public void setPseudoFlag(boolean pseudoFlag) {
		this.pseudoFlag = pseudoFlag;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Dangles getDangles() {
		return dangles;
	}

	public void setDangles(Dangles dangles) {
		this.dangles = dangles;
	}
}