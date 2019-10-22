/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx.model;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.AbstractModel;
import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "cdx_clinics")
public class CdxClinics extends AbstractModel<Integer> implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "clinicId")
    private String clinicId;
    @Column(name = "clinicName")
    private String clinicName;
    @Column(name = "clinicAddress")
    private String clinicAddress;


    @Override
    public Integer getId() {
        return(id);
    }


    @Override
    public int hashCode()
    {
        if (clinicId == null)
        {

            return(super.hashCode());
        }

        return(clinicId.hashCode());
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = StringUtils.trimToNull(clinicId);
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = StringUtils.trimToNull(clinicName);
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = StringUtils.trimToNull(clinicAddress);
    }
}
