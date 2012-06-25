/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

public class BillingBatchHeaderData {
	String id;
	String disk_id;
	String transc_id;
	String rec_id;
	String spec_id;
	String moh_office;
	String batch_id;
	String operator;
	String group_num;
	String provider_reg_num;
	String specialty;
	String h_count;
	String r_count;
	String t_count;
	String batch_date;

	String createdatetime;
	String updatedatetime;
	String creator;
	String action;
	String comment;

	public String getBatch_date() {
		return batch_date;
	}

	public void setBatch_date(String batch_date) {
		this.batch_date = batch_date;
	}

	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public String getDisk_id() {
		return disk_id;
	}

	public void setDisk_id(String disk_id) {
		this.disk_id = disk_id;
	}

	public String getGroup_num() {
		return group_num;
	}

	public void setGroup_num(String group_num) {
		this.group_num = group_num;
	}

	public String getH_count() {
		return h_count;
	}

	public void setH_count(String h_count) {
		this.h_count = h_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMoh_office() {
		return moh_office;
	}

	public void setMoh_office(String moh_office) {
		this.moh_office = moh_office;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProvider_reg_num() {
		return provider_reg_num;
	}

	public void setProvider_reg_num(String provider_reg_num) {
		this.provider_reg_num = provider_reg_num;
	}

	public String getR_count() {
		return r_count;
	}

	public void setR_count(String r_count) {
		this.r_count = r_count;
	}

	public String getRec_id() {
		return rec_id;
	}

	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getT_count() {
		return t_count;
	}

	public void setT_count(String t_count) {
		this.t_count = t_count;
	}

	public String getTransc_id() {
		return transc_id;
	}

	public void setTransc_id(String transc_id) {
		this.transc_id = transc_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdatedatetime() {
		return updatedatetime;
	}

	public void setUpdatedatetime(String updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

}
