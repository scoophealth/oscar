package oscar.oscarRx.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the favoritesprivilege table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="favoritesprivilege"
 */

public abstract class BaseFavoritesprivilege  implements Serializable {

	public static String REF = "Favoritesprivilege";
	public static String PROP_PROVIDER_NO = "ProviderNo";
	public static String PROP_WRITEABLE = "Writeable";
	public static String PROP_OPENTOPUBLIC = "Opentopublic";
	public static String PROP_ID = "Id";


	// constructors
	public BaseFavoritesprivilege () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseFavoritesprivilege (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String providerNo;
	private boolean opentopublic;
	private boolean writeable;



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
	 * Return the value associated with the column: provider_no
	 */
	public java.lang.String getProviderNo () {
		return providerNo;
	}

	/**
	 * Set the value related to the column: provider_no
	 * @param providerNo the provider_no value
	 */
	public void setProviderNo (java.lang.String providerNo) {
		this.providerNo = providerNo;
	}



	/**
	 * Return the value associated with the column: opentopublic
	 */
	public boolean isOpentopublic () {
		return opentopublic;
	}

	/**
	 * Set the value related to the column: opentopublic
	 * @param opentopublic the opentopublic value
	 */
	public void setOpentopublic (boolean opentopublic) {
		this.opentopublic = opentopublic;
	}



	/**
	 * Return the value associated with the column: writeable
	 */
	public boolean isWriteable () {
		return writeable;
	}

	/**
	 * Set the value related to the column: writeable
	 * @param writeable the writeable value
	 */
	public void setWriteable (boolean writeable) {
		this.writeable = writeable;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.oscarRx.model.Favoritesprivilege)) return false;
		else {
			oscar.oscarRx.model.Favoritesprivilege favoritesprivilege = (oscar.oscarRx.model.Favoritesprivilege) obj;
			if (null == this.getId() || null == favoritesprivilege.getId()) return false;
			else return (this.getId().equals(favoritesprivilege.getId()));
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