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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rjonasz
 */
@Entity
@Table(name = "oscar_msg_type")
@XmlRootElement
@NamedQueries({    
    @NamedQuery(name = "OscarMsgType.findByType", query = "SELECT o FROM OscarMsgType o WHERE o.Id = :type"),
    @NamedQuery(name = "OscarMsgType.findByDescription", query = "SELECT o FROM OscarMsgType o WHERE o.description = :description")})
public class OscarMsgType extends AbstractModel<Integer>implements Serializable {
    
    public static final Integer OSCAR_REVIEW_TYPE = 1;
    public static final Integer GENERAL_TYPE = 2;

    private static final long serialVersionUID = 1L;
    @Id    
    @Basic(optional = false)
    @Column(name = "type")
    private Integer Id;
    @Column(name = "description")
    private String description;
    
    public OscarMsgType() {
    }

    public OscarMsgType(Integer type) {
        this.Id = type;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    public void setId(Integer type) {
        this.Id = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OscarMsgType)) {
            return false;
        }
        OscarMsgType other = (OscarMsgType) object;
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.oscarehr.common.model.OscarMsgType[ type=" + Id + " ]";
    }
    
}
