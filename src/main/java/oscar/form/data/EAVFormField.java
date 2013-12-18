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

public class EAVFormField {
	
	private int field_id_eav;
	private int ref_form;
	private String field_name;
	private String field_type;
	private String sql_constraint;
	private String default_value;
	
	public EAVFormField()
	{
		
	}

	public int getField_id_eav() {
		return field_id_eav;
	}

	public void setField_id_eav(int field_id_eav) {
		this.field_id_eav = field_id_eav;
	}

	public int getRef_form() {
		return ref_form;
	}

	public void setRef_form(int ref_form) {
		this.ref_form = ref_form;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_type() {
		return field_type;
	}

	public void setField_type(String field_type) {
		this.field_type = field_type;
	}

	public String getSql_constraint() {
		return sql_constraint;
	}

	public void setSql_constraint(String field_constraint) {
		this.sql_constraint = field_constraint;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
	
	

}
