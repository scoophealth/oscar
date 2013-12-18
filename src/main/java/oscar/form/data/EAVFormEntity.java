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

package oscar.form.data;

import java.sql.Date;
import java.sql.Timestamp;

public class EAVFormEntity {
	
	private int entity_id_eav;
	private int ref_form;
	private int demographic_no;
	private int id;
	private Date formCreated;
	private Timestamp formEdited;

	public EAVFormEntity() {

	}

	public int getEntity_id_eav() {
    	return entity_id_eav;
    }

	public void setEntity_id_eav(int entity_id_eav) {
    	this.entity_id_eav = entity_id_eav;
    }

	public int getRef_form() {
    	return ref_form;
    }

	public void setRef_form(int ref_form) {
    	this.ref_form = ref_form;
    }

	public int getDemographic_no() {
    	return demographic_no;
    }

	public void setDemographic_no(int demographic_no) {
    	this.demographic_no = demographic_no;
    }

	public int getId() {
    	return id;
    }

	public void setId(int id) {
    	this.id = id;
    }

	public Date getFormCreated() {
    	return formCreated;
    }

	public void setFormCreated(Date formCreated) {
    	this.formCreated = formCreated;
    }

	public Timestamp getFormEdited() {
    	return formEdited;
    }

	public void setFormEdited(Timestamp formEdited) {
    	this.formEdited = formEdited;
    }

	

}
