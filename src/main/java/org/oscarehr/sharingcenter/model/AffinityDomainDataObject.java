/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.sharingcenter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.configuration.IheAffinityDomainPermission;
import org.marc.shic.core.configuration.IheCodeMappingConfiguration;
import org.marc.shic.core.configuration.IheValueSetConfiguration;
import org.marc.shic.core.configuration.consent.PolicyCollection;
import org.marc.shic.core.configuration.consent.PolicyDefinition;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.util.MiscUtils;

@Entity
@Table(name = "sharing_affinity_domain")
public class AffinityDomainDataObject extends AbstractModel<Integer> implements Serializable {

	/**
	 * This comparator sorts EForm ascending based on the id
	 */
	public static final Comparator<AffinityDomainDataObject> FORM_NAME_COMPARATOR = new Comparator<AffinityDomainDataObject>() {
		public int compare(AffinityDomainDataObject first, AffinityDomainDataObject second) {
			return (first.id.compareTo(second.id));
		}
	};
	
	private static Logger logger = MiscUtils.getLogger();

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "oid")
	private String oid;

	@Column(name = "name")
	private String name;

	@Column(name = "permission")
	private String permission;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "affinityDomain", fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<CodeMappingDataObject> codeMappings;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "affinityDomain", fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<ValueSetDataObject> valueSets;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "affinityDomain", fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<ActorDataObject> actors;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "affinityDomain", fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<PolicyDefinitionDataObject> policyDefinitions; //aka a PolicyCollection

	public AffinityDomainDataObject() {
	}

	public AffinityDomainDataObject(IheAffinityDomainConfiguration a) {
		this.oid = a.getOid();
		this.permission = a.getPermission().toString();
		this.name = a.getName();
		this.buildPolicyDefinitions(a.getConsent());
		this.buildActors(a.getActors());
		this.buildValueSets(a.getValueSets());
		this.buildCodeMappings(a.getCodeMappings());
	}
	
	public static IheAffinityDomainConfiguration createIheAffinityDomainConfiguration(AffinityDomainDataObject data) {
		IheAffinityDomainConfiguration a = new IheAffinityDomainConfiguration(data.getName());
		a.setOid(data.getOid());
	    a.setPermission(data.getPermissionEnum());
		a.setConsent(createPolicyCollection(data));
		
		a.setActors(createIheActorConfigurations(data));
		
		
		addIheCodeMappingConfigurations(a, data);
		addIheValueSetConfigurations(a, data);
		return a;
	}

	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return the permission
	 */
	public IheAffinityDomainPermission getPermissionEnum() {
		IheAffinityDomainPermission retVal = null;
		try {
			retVal = IheAffinityDomainPermission.fromString(permission);
        } catch (IheConfigurationException e) {
        	logger.error("Problem while getting affinity domain permission", e);
        }
		return retVal;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(IheAffinityDomainPermission permission) {
		this.permission = permission.toString();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the codeMappings
	 */
	public Set<CodeMappingDataObject> getCodeMappings() {
		return codeMappings;
	}

	/**
	 * @param codeMappings the codeMappings to set
	 */
	public void setCodeMappings(Set<CodeMappingDataObject> codeMappings) {
		this.codeMappings = codeMappings;
	}

	public void buildCodeMappings(List<IheCodeMappingConfiguration> iheCodeMappings) {
		for (IheCodeMappingConfiguration cm : iheCodeMappings) {
			this.addCodeMapping(new CodeMappingDataObject(cm));
		}
	}

	/**
	 * @return the valueSets
	 */
	public Set<ValueSetDataObject> getValueSets() {
		return valueSets;
	}

	/**
	 * @param valueSets the valueSets to set
	 */
	public void setValueSets(Set<ValueSetDataObject> valueSets) {
		this.valueSets = valueSets;
	}

	public void buildValueSets(List<IheValueSetConfiguration> iheValueSets) {
		for (IheValueSetConfiguration vs : iheValueSets) {
			this.addValueSet(new ValueSetDataObject(vs));
		}
	}

	/**
	 * @return the actors
	 */
	public Set<ActorDataObject> getActors() {
		return actors;
	}

	/**
	 * @param actors the actors to set
	 */
	public void setActors(Set<ActorDataObject> actors) {
		this.actors = actors;
	}

	public void buildActors(List<IheActorConfiguration> iheActors) {
		for (IheActorConfiguration a : iheActors) {
			this.addActor(new ActorDataObject(a));
		}
	}

	/**
	 * @return the policyDefinitions
	 */
	public Set<PolicyDefinitionDataObject> getPolicyDefinitions() {
		return policyDefinitions;
	}

	/**
	 * @param policyDefinitions the policyDefinitions to set
	 */
	public void setPolicyDefinitions(Set<PolicyDefinitionDataObject> policyDefinitions) {
		this.policyDefinitions = policyDefinitions;
	}

	public void buildPolicyDefinitions(PolicyCollection policyCollection) {
		for (PolicyDefinition policy : policyCollection.getPolicies()) {
			this.addPolicyDefinition(new PolicyDefinitionDataObject(policy));
		}
	}

	public void addActor(ActorDataObject actor) {
		if (actor != null) {
			if (this.actors == null) {
				this.actors = new HashSet<ActorDataObject>();
			}
			int one = actor.hashCode();
			this.actors.add(actor);
			actor.affinityDomain = this;
		}
	}

	public void addPolicyDefinition(PolicyDefinitionDataObject policy) {
		if (policy != null) {
			if (this.policyDefinitions == null) {
				this.policyDefinitions = new HashSet<PolicyDefinitionDataObject>();
			}
			this.policyDefinitions.add(policy);
			policy.affinityDomain = this;
		}
	}

	public void addCodeMapping(CodeMappingDataObject codeMapping) {
		if (codeMapping != null) {
			if (this.codeMappings == null) {
				this.codeMappings = new HashSet<CodeMappingDataObject>();
			}
			this.codeMappings.add(codeMapping);
			codeMapping.affinityDomain = this;
		}
	}

	public void addValueSet(ValueSetDataObject valueSet) {
		if (valueSet != null) {
			if (this.valueSets == null) {
				this.valueSets = new HashSet<ValueSetDataObject>();
			}
			this.valueSets.add(valueSet);
			valueSet.affinityDomain = this;
		}
	}

	private static ArrayList<IheActorConfiguration> createIheActorConfigurations(AffinityDomainDataObject data) {
		ArrayList<IheActorConfiguration> set = new ArrayList<IheActorConfiguration>();
		for(ActorDataObject a : data.getActors()) {
			set.add(a.createIheActorConfiguration());
		}
		return set;
	}

	private static void addIheValueSetConfigurations(IheAffinityDomainConfiguration config, AffinityDomainDataObject data) {
		for(ValueSetDataObject v : data.getValueSets()) {
			config.addValueSet(v.createIheValueSetConfiguration());
		}
	}

	private static void addIheCodeMappingConfigurations(IheAffinityDomainConfiguration config, AffinityDomainDataObject data) {
		for(CodeMappingDataObject c : data.getCodeMappings()) {
			config.addCodeMapping(c.createCodeMappingConfiguration());
		}
	}

	private static PolicyCollection createPolicyCollection(AffinityDomainDataObject data) {
		List<PolicyDefinition> list = new ArrayList<PolicyDefinition>();
		for(PolicyDefinitionDataObject p : data.getPolicyDefinitions()) {
			list.add(p.createPolicyDefinition());
		}
		return new PolicyCollection(list);
	}
	
	public CodeMappingDataObject getCodeMapping(String attribute) {
		CodeMappingDataObject retVal = null;
		
		for (CodeMappingDataObject mapping : this.codeMappings) {
			if (attribute.equalsIgnoreCase(mapping.getAttribute())) {
				retVal = mapping;
				break;
			}
		}
		
		return retVal;
	}

}
