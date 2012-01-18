package oscar.oscarEncounter.oscarMeasurements.model;

import java.io.Serializable;

/**
 * This is an object that contains data related to the measurementType table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="measurementType"
 */
public class Measurementtype implements Serializable {

	public static String REF = "Measurementtype";
	public static String PROP_TYPE = "type";
	public static String PROP_TYPE_DISPLAY_NAME = "typeDisplayName";
	public static String PROP_MEASURING_INSTRUCTION = "measuringInstruction";
	public static String PROP_TYPE_DESCRIPTION = "typeDescription";
	public static String PROP_VALIDATION = "validation";
	public static String PROP_ID = "id";

	private int hashCode = Integer.MIN_VALUE;
	private Integer id;
	private String type;
	private String typeDisplayName;
	private String typeDescription;
	private String measuringInstruction;
	private String validation;

	public Measurementtype() {
	}

	/**
	 * Constructor for primary key
	 */
	public Measurementtype(Integer id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Measurementtype(Integer id, String type, String typeDisplayName, String typeDescription, String measuringInstruction, String validation) {
		this.setId(id);
		this.setType(type);
		this.setTypeDisplayName(typeDisplayName);
		this.setTypeDescription(typeDescription);
		this.setMeasuringInstruction(measuringInstruction);
		this.setValidation(validation);
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
	 * Return the value associated with the column: typeDisplayName
	 */
	public String getTypeDisplayName() {
		return typeDisplayName;
	}

	/**
	 * Set the value related to the column: typeDisplayName
	 * 
	 * @param typeDisplayName the typeDisplayName value
	 */
	public void setTypeDisplayName(String typeDisplayName) {
		this.typeDisplayName = typeDisplayName;
	}

	/**
	 * Return the value associated with the column: typeDescription
	 */
	public String getTypeDescription() {
		return typeDescription;
	}

	/**
	 * Set the value related to the column: typeDescription
	 * 
	 * @param typeDescription the typeDescription value
	 */
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
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
	 * Return the value associated with the column: validation
	 */
	public String getValidation() {
		return validation;
	}

	/**
	 * Set the value related to the column: validation
	 * 
	 * @param validation the validation value
	 */
	public void setValidation(String validation) {
		this.validation = validation;
	}

	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.oscarEncounter.oscarMeasurements.model.Measurementtype)) return false;
		else {
			oscar.oscarEncounter.oscarMeasurements.model.Measurementtype measurementtype = (oscar.oscarEncounter.oscarMeasurements.model.Measurementtype) obj;
			if (null == this.getId() || null == measurementtype.getId()) return false;
			else return (this.getId().equals(measurementtype.getId()));
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