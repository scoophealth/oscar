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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="msgDemoMap")
public class MsgDemoMap extends AbstractModel<Long> {

	@Id
        @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
        
        private Integer messageID;
        private Integer demographic_no;
        

	public Long getId() {
            return id;
        }

	public void setId(Long id) {
		this.id = id;
	}

    /**
     * @return the messageID
     */
    public Integer getMessageID() {
        return messageID;
    }

    /**
     * @param messageID the messageID to set
     */
    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    /**
     * @return the demographic_no
     */
    public Integer getDemographic_no() {
        return demographic_no;
    }

    /**
     * @param demographic_no the demographic_no to set
     */
    public void setDemographic_no(Integer demographic_no) {
        this.demographic_no = demographic_no;
    }
        
        
	
	
}
