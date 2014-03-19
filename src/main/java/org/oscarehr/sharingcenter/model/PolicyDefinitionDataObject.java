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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.marc.shic.core.configuration.consent.AclDefinition;
import org.marc.shic.core.configuration.consent.PolicyDefinition;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_policy_definition")
public class PolicyDefinitionDataObject extends AbstractModel<Integer> implements Serializable
{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "code_system")
    private String codeSystem;
    
    @Column(name = "policy_doc_url")
    private String policyDocUrl;
    
    @Column(name = "ack_duration")
    private Double ackDuration;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_fk")
    public AffinityDomainDataObject affinityDomain;
    
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "policyDefinition", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<AclDefinitionDataObject> aclDefinitions;

    public PolicyDefinitionDataObject()
    {
    }

    public PolicyDefinitionDataObject(String displayName, String code, String codeSystem, String policyDocUrl, Double ackDuration)
    {
        this.displayName = displayName;
        this.code = code;
        this.codeSystem = codeSystem;
        this.policyDocUrl = policyDocUrl;
        this.ackDuration = ackDuration;
    }

    public PolicyDefinitionDataObject(PolicyDefinition policy)
    {
        this.displayName = policy.getDisplayName();
        this.code = policy.getCode();
        this.codeSystem = policy.getCodeSystem();
        this.policyDocUrl = policy.getPolicyDocumentUrl();
        this.ackDuration = policy.getAcknowledgementDuration();
        this.buildAclDefinitions(policy.getAcl());
    }

    @Override
    public Integer getId()
    {
        return id;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem()
    {
        return codeSystem;
    }

    /**
     * @param codeSystem the codeSystem to set
     */
    public void setCodeSystem(String codeSystem)
    {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the policyDocUrl
     */
    public String getPolicyDocUrl()
    {
        return policyDocUrl;
    }

    /**
     * @param policyDocUrl the policyDocUrl to set
     */
    public void setPolicyDocUrl(String policyDocUrl)
    {
        this.policyDocUrl = policyDocUrl;
    }

    /**
     * @return the ackDuration
     */
    public Double getAckDuration()
    {
        return ackDuration;
    }

    /**
     * @param ackDuration the ackDuration to set
     */
    public void setAckDuration(Double ackDuration)
    {
        this.ackDuration = ackDuration;
    }

    /**
     * @return the affinityDomain
     */
    public AffinityDomainDataObject getAffinityDomain()
    {
        return affinityDomain;
    }

    /**
     * @param affinityDomain the affinityDomain to set
     */
    public void setAffinityDomain(AffinityDomainDataObject affinityDomain)
    {
        this.affinityDomain = affinityDomain;
    }

    /**
     * @return the aclDefinitions
     */
    public Set<AclDefinitionDataObject> getAclDefinitions()
    {
        return aclDefinitions;
    }

    private void buildAclDefinitions(List<AclDefinition> acl)
    {
        for (AclDefinition a : acl)
        {
            this.addAclDefinition(new AclDefinitionDataObject(a));
        }
    }

    /**
     * @param aclDefinitions the aclDefinitions to set
     */
    public void setAclDefinitions(Set<AclDefinitionDataObject> aclDefinitions)
    {
        this.aclDefinitions = aclDefinitions;
    }

    public void addAclDefinition(AclDefinitionDataObject aclDefinition)
    {
        if (aclDefinition != null)
        {
            if (this.aclDefinitions == null)
            {
                this.aclDefinitions = new HashSet<AclDefinitionDataObject>();
            }
            this.aclDefinitions.add(aclDefinition);
            aclDefinition.policyDefinition = this;
        }
    }

    public PolicyDefinition createPolicyDefinition()
    {
        PolicyDefinition p = new PolicyDefinition();
        p.setCode(code);
        p.setCodeSystem(codeSystem);
        p.setDisplayName(displayName);
        p.setPolicyDocumentUrl(policyDocUrl);
        p.setAcknowledgementDuration(ackDuration);
        for (AclDefinitionDataObject acl : aclDefinitions)
        {
            p.addAclDefinition(acl.createAclDefinition());
        }
        return p;
    }
}