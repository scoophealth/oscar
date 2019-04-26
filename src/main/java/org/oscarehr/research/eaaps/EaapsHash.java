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
package org.oscarehr.research.eaaps;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;

import org.oscarehr.common.model.Demographic;

import oscar.util.Appender;
import oscar.util.ConversionUtils;

/**
 * Hashing function for generating a one way hash that can identify the patient based on the
 * demographic information available in the system. 
 *
 */
public class EaapsHash {

	private String key;
	private String hash;

	public EaapsHash(Demographic d) {
		this(d, null);
	}
	
	public EaapsHash(Demographic d, String clinic) {
		Appender appender = new Appender("");
		
		GregorianCalendar calendar = d.getBirthDay();
		if (calendar != null) {
			appender.append(ConversionUtils.toDateString(calendar.getTime(), "yyyyMMdd"));
		}
		if(d.getHin() != null) {
			appender.append(d.getHin().trim());
		}

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unsupported algorithm", e);
		}
		digest.reset();
		// digest.update("eaaps".getBytes());

		String key = appender.toString();

		byte[] bytes = digest.digest(key.getBytes());
		
        //convert the byte to hex
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
          buf.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

		setKey(key);
		setHash(buf.toString());
		
		// setHash(new String(Base64.encodeBase64(bytes)));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
