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

	public boolean equals(OcanClientFormData o) {
		try {
			return (id != null && id.intValue() == o.id.intValue());
		} catch (Exception e) {
			return (false);
		}
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
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
