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
 * This entity represents key value pairs associated with the CdsClientForm object.
 * Generally speaking the key is the question asked and is specified by us, the answer
 * is generally the CDS category like "010-4" or something like that.
 */
@Entity
public class CdsClientFormData extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer cdsClientFormId = null;
	private String question = null;
	private String answer = null;

	@Override
    public Integer getId() {
		return id;
	}

	public Integer getCdsClientFormId() {
		return cdsClientFormId;
	}

	public void setCdsClientFormId(Integer cdsClientFormId) {
		this.cdsClientFormId = cdsClientFormId;
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
	public static boolean containsAnswer(List<CdsClientFormData> answers, String answer)
	{
		if (answer==null) return(false);
		
		for (CdsClientFormData data : answers)
		{
			if (answer.equals(data.getAnswer())) return(true);
		}
		
		return(false);
	}
}
