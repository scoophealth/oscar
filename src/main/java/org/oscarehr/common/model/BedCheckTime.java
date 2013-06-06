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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

@Entity
@Table(name="bed_check_time")
public class BedCheckTime extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bed_check_time_id")
	private Integer id;
	
	@Column(name="program_id")
	private Integer programId;
	
	@Column(name="bed_check_time")
	@Temporal(TemporalType.TIME)
	private Date time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
    public static BedCheckTime create(Integer programId, String time) {
		BedCheckTime bedCheckTime = new BedCheckTime();
		bedCheckTime.setProgramId(programId);
		bedCheckTime.setStrTime(time);
		
		return bedCheckTime;
	}
    
    public String getStrTime() {
 		return DateTimeFormatUtils.getStringFromTime(getTime());
 	}

 	// property adapted for view
 	public void setStrTime(String strTime) {
 		setTime(DateTimeFormatUtils.getTimeFromString(strTime));
 	}
	

	
}
