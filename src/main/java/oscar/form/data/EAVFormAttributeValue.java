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
import java.sql.Time;

public class EAVFormAttributeValue {

	private int ref_form_entity;
	private int ref_form;
	private int ref_field;
	private String attribute_value_varchar;
	private Time attribute_value_time;
	private String attribute_value_text;
	private Integer attribute_value_int_unsigned;
	private Integer attribute_value_int;
	private String attribute_value_enum;
	private Double attribute_value_double;
	private Date attribute_value_date;
	private Character attribute_value_char;
	private Integer attribute_value_bit;
	private Integer attribute_value_bigint;
	private String attribute_name;

	public EAVFormAttributeValue() {

	}

	public int getRef_form_entity() {
		return ref_form_entity;
	}

	public void setRef_form_entity(int ref_form_entity) {
		this.ref_form_entity = ref_form_entity;
	}

	public int getRef_form() {
		return ref_form;
	}

	public void setRef_form(int ref_form) {
		this.ref_form = ref_form;
	}

	public int getRef_field() {
		return ref_field;
	}

	public void setRef_field(int ref_field) {
		this.ref_field = ref_field;
	}

	public String getAttribute_value_varchar() {
		return attribute_value_varchar;
	}

	public void setAttribute_value_varchar(String attribute_value_varchar) {
		this.attribute_value_varchar = attribute_value_varchar;
	}

	public Time getAttribute_value_time() {
		return attribute_value_time;
	}

	public void setAttribute_value_time(Time attribute_value_time) {
		this.attribute_value_time = attribute_value_time;
	}

	public String getAttribute_value_text() {
		return attribute_value_text;
	}

	public void setAttribute_value_text(String attribute_value_text) {
		this.attribute_value_text = attribute_value_text;
	}

	public Integer getAttribute_value_int_unsigned() {
		return attribute_value_int_unsigned;
	}

	public void setAttribute_value_int_unsigned(Integer attribute_value_int_unsigned) {
		this.attribute_value_int_unsigned = attribute_value_int_unsigned;
	}

	public Integer getAttribute_value_int() {
		return attribute_value_int;
	}

	public void setAttribute_value_int(Integer attribute_value_int) {
		this.attribute_value_int = attribute_value_int;
	}

	public String getAttribute_value_enum() {
		return attribute_value_enum;
	}

	public void setAttribute_value_enum(String attribute_value_enum) {
		this.attribute_value_enum = attribute_value_enum;
	}

	public Double getAttribute_value_double() {
		return attribute_value_double;
	}

	public void setAttribute_value_double(Double attribute_value_double) {
		this.attribute_value_double = attribute_value_double;
	}

	public Date getAttribute_value_date() {
		return attribute_value_date;
	}

	public void setAttribute_value_date(Date attribute_value_date) {
		this.attribute_value_date = attribute_value_date;
	}

	public Character getAttribute_value_char() {
		return attribute_value_char;
	}

	public void setAttribute_value_char(Character attribute_value_char) {
		this.attribute_value_char = attribute_value_char;
	}

	public Integer getAttribute_value_bit() {
		return attribute_value_bit;
	}

	public void setAttribute_value_bit(Integer attribute_value_bit) {
		this.attribute_value_bit = attribute_value_bit;
	}

	public Integer getAttribute_value_bigint() {
		return attribute_value_bigint;
	}

	public void setAttribute_value_bigint(Integer attribute_value_bigint) {
		this.attribute_value_bigint = attribute_value_bigint;
	}

	public String getAttribute_name() {
		return attribute_name;
	}

	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}

	

}
