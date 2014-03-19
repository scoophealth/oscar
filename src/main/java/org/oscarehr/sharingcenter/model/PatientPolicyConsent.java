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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_patient_policy_consent")
public class PatientPolicyConsent extends AbstractModel<Integer> implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "demographic_no")
    private Integer demographicNo;
    @Column(name = "affinity_domain_id")
    private Integer affinityDomainId;
    @Column(name = "consent_date", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date consentDate;
    @Column(name = "policy_id")
    private Integer policyId;

    @Override
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getAffinityDomainId()
    {
        return affinityDomainId;
    }

    public void setAffinityDomainId(Integer affinityDomainId)
    {
        this.affinityDomainId = affinityDomainId;
    }

    public Integer getDemographicNo()
    {
        return demographicNo;
    }

    public void setDemographicNo(Integer demographicNo)
    {
        this.demographicNo = demographicNo;
    }

    public Date getConsentDate()
    {
        return consentDate;
    }

    public void setConsentDate(Date consentDate)
    {
        this.consentDate = consentDate;
    }

    public Integer getPolicyId()
    {
        return policyId;
    }

    public void setPolicyId(Integer policyId)
    {
        this.policyId = policyId;
    }
}
