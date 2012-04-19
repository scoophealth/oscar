/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;


@Entity 
public class SentToPHRTracking extends AbstractModel<Long> {
	
    @Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(nullable = false)
	private Integer demographicNo;
    @Column(nullable = false)
	private String objectName;
    @Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDatetime;  
    @Column(nullable = false)
	private String sentToServer;
    
	
	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = StringUtils.trimToNull(objectName);
	}
	
	public Date getSentDatetime() {
		return sentDatetime;
	}
	public void setSentDatetime(Date sentDatetime) {
		this.sentDatetime = sentDatetime;
	}
	
	public String getSentToServer() {
		return sentToServer;
	}
	public void setSentToServer(String sentToServer) {
		this.sentToServer = StringUtils.trimToNull(sentToServer);
	}
	@Override
	public Long getId() {
		return id;
	}
}
