package edu.banda.coel.domain.net;

import com.banda.core.domain.TechnicalDomainObject;

import java.util.List;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class NetworkConfiguration extends TechnicalDomainObject {

    private List<Byte> states;
    private Integer step;

    public List<Byte> getStates() {
        return states;
    }

    public void setStates(List<Byte> states) {
        this.states = states;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (step != null) {
            sb.append(step);
            sb.append(" / ");
        }
        sb.append(states.size());
        return sb.toString();
    }
}