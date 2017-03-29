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

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * This entity represents key value pairs associated with the OcanClientForm object.
 * Generally speaking the key is the question asked and is specified by us, the answer
 * is generally the CDS category like "010-4" or something like that. There
 * are a few exceptions where the value maybe a number like "number of days hospitalised"
 * we are not able to use the cds category.
 */
@Entity
public class OcanClientFormData extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer ocanClientFormId = null;
	private String question = null;
	private String answer = null;

	public Integer getId() {
		return id;
	}

	public Integer getOcanClientFormId() {
		return ocanClientFormId;
	}

	public void setOcanClientFormId(Integer ocanClientFormId) {
		this.ocanClientFormId = ocanClientFormId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		OcanClientFormData other = (OcanClientFormData) obj;
		if (answer == null) {
			if (other.answer != null) return false;
		} else if (!answer.equals(other.answer)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@PreRemove
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpaPreventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

	/**
	 * @return true if the list of cdsClientFormData contains the answer specified. 
	 */
	public static boolean containsAnswer(List<OcanClientFormData> answers, String answer)
	{
		if (answer==null) return(false);
		
		for (OcanClientFormData data : answers)
		{
			if (answer.equals(data.getAnswer())) return(true);
		}
		
		return(false);
	}
}
