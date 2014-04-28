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

package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author mweston4
 */

@Entity
@Table(name="frm_labreq_preset")
public class FrmLabReqPreSet extends AbstractModel<Integer> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "preset_id", nullable = false)
    private Integer id;
    
    @Column(name = "lab_type", nullable = false)    
    private String labType;
    
    @Column(name = "prop_name", nullable = false)    
    private String propertyName;
    
    @Column(name = "prop_value", nullable = false)    
    private String propertyValue;
        
    @Column(name = "status", nullable = false)    
    private Integer status;
     
    @Override
    public Integer getId() {
            return id;
    }
    
    public String getLabType() { return this.labType; }
    protected void setLabType(String labType) { this.labType = labType; }
    
    public String getPropertyName() { return this.propertyName; }
    protected void setPropertyName(String propertyName) { this.propertyName = propertyName; }
    
    public String getPropertyValue() { return this.propertyValue; }
    protected void setPropertyValue(String propertyValue) { this.propertyValue = propertyValue; }
    
    public Integer getStatus() { return this.status; }
    protected void setStatus(Integer status) { this.status = status; }
}
