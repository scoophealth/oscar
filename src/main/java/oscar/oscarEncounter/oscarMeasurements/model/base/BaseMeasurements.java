package oscar.oscarEncounter.oscarMeasurements.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the measurements table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="measurements"
 */

public abstract class BaseMeasurements  implements Serializable {

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


	// constructors
	public BaseMeasurements () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseMeasurements (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseMeasurements (
		java.lang.Integer id,
		java.lang.String type,
		java.lang.Integer demographicNo,
		java.lang.String providerNo,
		java.lang.String dataField,
		java.lang.String measuringInstruction,
		java.lang.String comments,
		java.util.Date dateObserved,
		java.util.Date dateEntered) {

		this.setId(id);
		this.setType(type);
		this.setDemographicNo(demographicNo);
		this.setProviderNo(providerNo);
		this.setDataField(dataField);
		this.setMeasuringInstruction(measuringInstruction);
		this.setComments(comments);
		this.setDateObserved(dateObserved);
		this.setDateEntered(dateEntered);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String type;
	private java.lang.Integer demographicNo;
	private java.lang.String providerNo;
	private java.lang.String dataField;
	private java.lang.String measuringInstruction;
	private java.lang.String comments;
	private java.util.Date dateObserved;
	private java.util.Date dateEntered;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: type
	 */
	public java.lang.String getType () {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * @param type the type value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: demographicNo
	 */
	public java.lang.Integer getDemographicNo () {
		return demographicNo;
	}

	/**
	 * Set the value related to the column: demographicNo
	 * @param demographicNo the demographicNo value
	 */
	public void setDemographicNo (java.lang.Integer demographicNo) {
		this.demographicNo = demographicNo;
	}



	/**
	 * Return the value associated with the column: providerNo
	 */
	public java.lang.String getProviderNo () {
		return providerNo;
	}

	/**
	 * Set the value related to the column: providerNo
	 * @param providerNo the providerNo value
	 */
	public void setProviderNo (java.lang.String providerNo) {
		this.providerNo = providerNo;
	}



	/**
	 * Return the value associated with the column: dataField
	 */
	public java.lang.String getDataField () {
		return dataField;
	}

	/**
	 * Set the value related to the column: dataField
	 * @param dataField the dataField value
	 */
	public void setDataField (java.lang.String dataField) {
		this.dataField = dataField;
	}



	/**
	 * Return the value associated with the column: measuringInstruction
	 */
	public java.lang.String getMeasuringInstruction () {
		return measuringInstruction;
	}

	/**
	 * Set the value related to the column: measuringInstruction
	 * @param measuringInstruction the measuringInstruction value
	 */
	public void setMeasuringInstruction (java.lang.String measuringInstruction) {
		this.measuringInstruction = measuringInstruction;
	}



	/**
	 * Return the value associated with the column: comments
	 */
	public java.lang.String getComments () {
		return comments;
	}

	/**
	 * Set the value related to the column: comments
	 * @param comments the comments value
	 */
	public void setComments (java.lang.String comments) {
		this.comments = comments;
	}



	/**
	 * Return the value associated with the column: dateObserved
	 */
	public java.util.Date getDateObserved () {
		return dateObserved;
	}

	/**
	 * Set the value related to the column: dateObserved
	 * @param dateObserved the dateObserved value
	 */
	public void setDateObserved (java.util.Date dateObserved) {
		this.dateObserved = dateObserved;
	}



	/**
	 * Return the value associated with the column: dateEntered
	 */
	public java.util.Date getDateEntered () {
		return dateEntered;
	}

	/**
	 * Set the value related to the column: dateEntered
	 * @param dateEntered the dateEntered value
	 */
	public void setDateEntered (java.util.Date dateEntered) {
		this.dateEntered = dateEntered;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.oscarEncounter.oscarMeasurements.model.Measurements)) return false;
		else {
			oscar.oscarEncounter.oscarMeasurements.model.Measurements measurements = (oscar.oscarEncounter.oscarMeasurements.model.Measurements) obj;
			if (null == this.getId() || null == measurements.getId()) return false;
			else return (this.getId().equals(measurements.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}