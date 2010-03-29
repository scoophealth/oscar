/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */
package oscar.eform.model;

import java.io.Serializable;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class EformValue implements Serializable {
    private Integer id;
    private Integer fdid;
    private Integer fid;
    private Integer demographic_no;
    private String  var_name;
    private String  var_value;

    public Integer getId() {
        return this.id;
    }

    public Integer getFdid() {
        return this.fdid;
    }

    public Integer getFid() {
        return this.fid;
    }

    public Integer getDemographic_no() {
        return this.demographic_no;
    }

    public String getVar_name() {
        return this.var_name;
    }

    public String getVar_value() {
        return this.var_value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFdid(Integer fdid) {
        this.fdid = fdid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public void setDemographic_no(Integer demographic_no) {
        this.demographic_no = demographic_no;
    }

    public void setVar_name(String var_name) {
        this.var_name = var_name;
    }

    public void setVar_value(String var_value) {
        this.var_value = var_value;
    }
}
