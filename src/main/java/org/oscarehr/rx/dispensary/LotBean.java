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
package org.oscarehr.rx.dispensary;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class LotBean {

	private String name;		//name of lot
	private Integer amount;  	//the amount of doses
	private Date expiryDate;	//expires

	private int numberAvailable = 0;
	
	public LotBean() {
		
	}
	
	public LotBean(String name, Date expiryDate, Integer amount) {
		this.name = name;
		this.expiryDate = expiryDate;
		this.amount= amount;
	}
	
	public LotBean(String name, Date expiryDate, Integer amount, int numberAvailable) {
		this(name,expiryDate,amount);
		this.numberAvailable = numberAvailable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getNumberAvailable() {
		return numberAvailable;
	}

	public void setNumberAvailable(int numberAvailable) {
		this.numberAvailable = numberAvailable;
	}
	
	
	public String getExpiryDateAsString() {
		return DateFormatUtils.ISO_DATE_FORMAT.format(expiryDate);
	}

	
	public String getHash() {
		return  DigestUtils.md5Hex(getName()+getExpiryDate()+getAmount());
	}

	
}
