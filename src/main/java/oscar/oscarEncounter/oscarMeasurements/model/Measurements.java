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

package oscar.oscarEncounter.oscarMeasurements.model;

import java.io.Serializable;

/**
 * @deprecated use MeasurementDao instead (2012-01-23)
 * 
 * This is an object that contains data related to the measurements table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="measurements"
 */
public class Measurements implements Serializable {
	public static String REF = "Measurements";
	public static String PROP_PROVIDER_NO = "providerNo";
	public static String PROP_TYPE = "type";
	public static String PROP_DATA_FIELD = "dataField";
	public static String PROP_DEMOGRAPHIC_NO = "demographicNo";
	public static String PROP_DATE_ENTERED = "dateEntered";
	public static String PROP_COMMENTS = "comments";
	public static String PROP_MEASURING_INSTRUCTION = "measuringInstruction";
	public static String PROP_DATE_OBSERVED = "dateObserved";
	public static String PROP_ID = "id";

	private int appointmentNo;
	private int hashCode = Integer.MIN_VALUE;
	private Integer id;
	private String type;
	private Integer demographicNo;
	private String providerNo;
	private String dataField;
	private String measuringInstruction;
	private String comments;
	private java.util.Date dateObserved;
	private java.util.Date dateEntered;

	public Measurements() {
	}

	/**
	 * Constructor for primary key
	 */
	public Measurements(Integer id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Measurements(Integer id, String type, Integer demographicNo, String providerNo, String dataField, String measuringInstruction, String comments, java.util.Date dateObserved, java.util.Date dateEntered) {

		this.setId(id);
		this.setType(type);
		this.setDemographicNo(demographicNo);
		this.setProviderNo(providerNo);
		this.setDataField(dataField);
		this.setMeasuringInstruction(measuringInstruction);
		this.setComments(comments);
		this.setDateObserved(dateObserved);
		this.setDateEntered(dateEntered);
	}

	public int getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="id"
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id the new ID
	 */
	public void setId(Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * 
	 * @param type the type value
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Return the value associated with the column: demographicNo
	 */
	public Integer getDemographicNo() {
		return demographicNo;
	}

	/**
	 * Set the value related to the column: demographicNo
	 * 
	 * @param demographicNo the demographicNo value
	 */
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	/**
	 * Return the value associated with the column: providerNo
	 */
	public String getProviderNo() {
		return providerNo;
	}

	/**
	 * Set the value related to the column: providerNo
	 * 
	 * @param providerNo the providerNo value
	 */
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	/**
	 * Return the value associated with the column: dataField
	 */
	public String getDataField() {
		return dataField;
	}

	/**
	 * Set the value related to the column: dataField
	 * 
	 * @param dataField the dataField value
	 */
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	/**
	 * Return the value associated with the column: measuringInstruction
	 */
	public String getMeasuringInstruction() {
		return measuringInstruction;
	}

	/**
	 * Set the value related to the column: measuringInstruction
	 * 
	 * @param measuringInstruction the measuringInstruction value
	 */
	public void setMeasuringInstruction(String measuringInstruction) {
		this.measuringInstruction = measuringInstruction;
	}

	/**
	 * Return the value associated with the column: comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Set the value related to the column: comments
	 * 
	 * @param comments the comments value
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Return the value associated with the column: dateObserved
	 */
	public java.util.Date getDateObserved() {
		return dateObserved;
	}

	/**
	 * Set the value related to the column: dateObserved
	 * 
	 * @param dateObserved the dateObserved value
	 */
	public void setDateObserved(java.util.Date dateObserved) {
		this.dateObserved = dateObserved;
	}

	/**
	 * Return the value associated with the column: dateEntered
	 */
	public java.util.Date getDateEntered() {
		return dateEntered;
	}

	/**
	 * Set the value related to the column: dateEntered
	 * 
	 * @param dateEntered the dateEntered value
	 */
	public void setDateEntered(java.util.Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.oscarEncounter.oscarMeasurements.model.Measurements)) return false;
		else {
			oscar.oscarEncounter.oscarMeasurements.model.Measurements measurements = (oscar.oscarEncounter.oscarMeasurements.model.Measurements) obj;
			if (null == this.getId() || null == measurements.getId()) return false;
			else return (this.getId().equals(measurements.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

}
