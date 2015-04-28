package edu.banda.coel.domain.net;

import com.banda.core.domain.TechnicalDomainObject;
import com.banda.core.domain.um.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class NetworkType extends TechnicalDomainObject {

    private String name;
    private Date createTime;
    private User createdBy;

    private NetworkType superType;
    private Set<NetworkType> subTypes = new HashSet<NetworkType>();
//	private Set<NetworkDefinition> netDefinitions = new HashSet<NetworkDefinition>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public NetworkType getSuperType() {
        return superType;
    }

    public void setSuperType(NetworkType superType) {
        this.superType = superType;
    }

    public Set<NetworkType> getSubTypes() {
        return subTypes;
    }

    protected void setSubTypes(Set<NetworkType> subTypes) {
        this.subTypes = subTypes;
    }

    public void addSubType(NetworkType subType) {
        subTypes.add(subType);
        subType.setSuperType(this);
    }

    public void removeSubType(NetworkType subType) {
        subTypes.remove(subType);
        subType.setSuperType(null);
    }

//	public Set<NetworkDefinition> getNetDefinitions() {
//		return netDefinitions;
//	}
//
//	protected void setNetDefinitions(Set<NetworkDefinition> netDefinitions) {
//		this.netDefinitions = netDefinitions;
//	}
//
//	protected void addNetDefinition(NetworkDefinition netDefinition) {
//		netDefinitions.add(netDefinition);
//	}
//
//	protected void removeNetDefinition(NetworkDefinition netDefinition) {
//		netDefinitions.remove(netDefinition);
//	}
}