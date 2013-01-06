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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.util.MiscUtils;

@Entity
@Table(name="hash_audit")
public class HashAudit extends AbstractModel<Integer> {

	public final static String NOTE = "enc";
	public final static String ALGORITHM = "MD5";
	 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pkid")
	private Integer id;

	private String signature;
	
	@Column(name="id")
	private String id2;
	
	private String type;
	
	private String algorithm = ALGORITHM;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	
    public boolean equals(Object o) {
        String h = this.getSignature();
        
        return (h.compareTo( ((HashAudit)o).getSignature()) == 0);
    }
    
    public int hashCode() {
        String h = this.getSignature();
        
        return h.hashCode();
    }
    
    
    public void makeHash(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(input);
            byte[] bHash = digest.digest();           
            StringBuilder tmp = new StringBuilder();
            for( int i = 0; i < bHash.length; ++i ) {
                tmp.append(Integer.toHexString((bHash[i] >>> 4) & 0x0F));
                tmp.append(Integer.toHexString(bHash[i] & 0x0F));
            }
            setSignature(tmp.toString());
        }        
        catch(NoSuchAlgorithmException e) {
            MiscUtils.getLogger().error("Error", e);
        }
     }     
}
