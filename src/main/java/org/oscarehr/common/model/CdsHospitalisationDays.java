package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents the days which some one was hospitalised as defined by CDS section 20.
 */
@Entity
public class CdsHospitalisationDays extends AbstractModel<Integer> implements Serializable {

	public static final Comparator<CdsHospitalisationDays> ADMISSION_DATE_COMPARATOR=new Comparator<CdsHospitalisationDays>()
	{
		public int compare(CdsHospitalisationDays o1, CdsHospitalisationDays o2) {
			return(o1.admitted.compareTo(o2.admitted));
		}	
	};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer clientId = null;

	@Temporal(TemporalType.DATE)
	private Calendar admitted = null;

	@Temporal(TemporalType.DATE)
	private Calendar discharged = null;

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Calendar getAdmitted() {
		return admitted;
	}

	public void setAdmitted(Calendar admitted) {
		this.admitted = admitted;
	}

	public Calendar getDischarged() {
		return discharged;
	}

	public void setDischarged(Calendar discharged) {
		this.discharged = discharged;
	}

}
