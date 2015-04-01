/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
package org.oscarehr.integration.mcedt.mailbox;

import java.math.BigInteger;
import java.util.Comparator;

import javax.xml.datatype.XMLGregorianCalendar;

import ca.ontario.health.edt.CommonResult;
import ca.ontario.health.edt.ResourceStatus;

public class DetailDataKai{
	protected XMLGregorianCalendar createTimestamp;
	protected String description;
	protected String resourceType;
	protected XMLGregorianCalendar modifyTimestamp;
	protected BigInteger resourceID;
	protected CommonResult result;
	protected ResourceStatus status;
	protected String downloadStatus;	
	
	public XMLGregorianCalendar getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(XMLGregorianCalendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public XMLGregorianCalendar getModifyTimestamp() {
		return modifyTimestamp;
	}
	public void setModifyTimestamp(XMLGregorianCalendar modifyTimestamp) {
		this.modifyTimestamp = modifyTimestamp;
	}
	public BigInteger getResourceID() {
		return resourceID;
	}
	public void setResourceID(BigInteger resourceID) {
		this.resourceID = resourceID;
	}
	public CommonResult getResult() {
		return result;
	}
	public void setResult(CommonResult result) {
		this.result = result;
	}
	public ResourceStatus getStatus() {
		return status;
	}
	public void setStatus(ResourceStatus status) {
		this.status = status;
	}	
	
	public String getDownloadStatus() {
		return downloadStatus;
	}
	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}


	public static Comparator<DetailDataKai> ResourceIdComparator = new Comparator<DetailDataKai> () {
		 public int compare(DetailDataKai detailData1,DetailDataKai detailData2) {
            String resourceID1 = detailData1.getResourceID().toString();
            String resourceID2 = detailData2.getResourceID().toString();
            
        return resourceID1.compareTo(resourceID2);
        }
	};		
	
}
