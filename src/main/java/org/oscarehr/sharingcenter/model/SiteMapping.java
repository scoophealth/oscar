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

import javax.persistence.*;
import org.marc.shic.core.CodeValue;
import org.marc.shic.core.MappingCodeType;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.util.MiscUtils;

@Entity
@Table(name = "sharing_mapping_site")
public class SiteMapping extends AbstractModel<Integer> implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "affinity_domain")
    private int affinityDomain;

    @Column(name = "source")
    private String source;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_type_code")
    private MappingCode facilityTypeCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "practice_setting_code")
    private MappingCode practiceSettingCode;

    public SiteMapping() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getAffinityDomain() {
        return affinityDomain;
    }

    public void setAffinityDomain(int affinityDomain) {
        this.affinityDomain = affinityDomain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MappingCode getFacilityTypeCode() {
        return facilityTypeCode;
    }

    public void setFacilityTypeCode(MappingCode facilityTypeCode) {
        this.facilityTypeCode = facilityTypeCode;
    }

    public MappingCode getPracticeSettingCode() {
        return practiceSettingCode;
    }

    public void setPracticeSettingCode(MappingCode practiceSettingCode) {
        this.practiceSettingCode = practiceSettingCode;
    }
    
    public CodeValue getCode(MappingCodeType codeType) {
        CodeValue retVal = null;
        
        if (codeType.equals(MappingCodeType.HealthcareFacilityTypeCode) && facilityTypeCode != null) {
            retVal = facilityTypeCode.generateCodeValue();
        } else if (codeType.equals(MappingCodeType.PracticeSettingCode) && practiceSettingCode != null) {
            retVal = practiceSettingCode.generateCodeValue();
        } else {
            MiscUtils.getLogger().warn("Unsupported Code " + codeType.getCode());
            retVal = new CodeValue("Unknown Code " + codeType.getCode(), "Unknown CodeSystem", "Unknown");
        }
        
        return retVal;
    }

}
