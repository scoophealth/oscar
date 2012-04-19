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


package org.oscarehr.casemgmt.model.base;

import java.io.Serializable;


/**
 *
 * @author rjonasz
 */
public abstract class BaseHashAudit implements Serializable {
    public final static String NOTE = "enc";
    protected StringBuilder signature;
    protected String type;
    protected String algorithm;
    private long id;
    private long pkid;  //primary key
    
    /** Creates a new instance of BaseHashAudit */
    public BaseHashAudit() {
        this.signature = new StringBuilder();
        this.id = 0;
    }
    
    public void setPkid(long pkid) {
        this.pkid = pkid;
    }
    
    public long getPkid() {
        return this.pkid;
    }
    
    public String getSignature() {
        return this.signature.toString();
    }
    
    public void setSignature(String signature) {
        this.signature = new StringBuilder(signature);
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setAlgorithm(String algrthm) {
        this.algorithm = algrthm;
    }
    
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    /*
     *This function must be implemented by derived classes
     *Multiple algorithms may then be used to calculate hash
     */
    public abstract void makeHash(byte[] input);
    
    public boolean equals(Object o) {
        BaseHashAudit rhs = (BaseHashAudit)o;
        String h = this.getSignature();
        
        return (h.compareTo(rhs.getSignature()) == 0);
    }
    
    public int hashCode() {
        String h = this.getSignature();
        
        return h.hashCode();
    }
    
}
