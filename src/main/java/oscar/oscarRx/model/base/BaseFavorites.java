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

package oscar.oscarRx.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the favorites table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="favorites"
 */

public abstract class BaseFavorites  implements Serializable {

	public static String REF = "Favorites";
	public static String PROP_UNIT_NAME = "UnitName";
	public static String PROP_ATC = "Atc";
	public static String PROP_PRN = "Prn";
	public static String PROP_SPECIAL = "Special";
	public static String PROP_CUSTOM_NAME = "CustomName";
	public static String PROP_GN = "Gn";
	public static String PROP_METHOD = "Method";
	public static String PROP_DURUNIT = "Durunit";
	public static String PROP_CUSTOM_INSTRUCTIONS = "CustomInstructions";
	public static String PROP_UNIT = "Unit";
	public static String PROP_DURATION = "Duration";
	public static String PROP_REPEAT = "Repeat";
	public static String PROP_TAKEMAX = "Takemax";
	public static String PROP_ROUTE = "Route";
	public static String PROP_DRUGFORM = "DrugForm";
	public static String PROP_FAVORITENAME = "Favoritename";
	public static String PROP_QUANTITY = "Quantity";
	public static String PROP_PROVIDER_NO = "ProviderNo";
	public static String PROP_TAKEMIN = "Takemin";
	public static String PROP_REGIONAL_IDENTIFIER = "RegionalIdentifier";
	public static String PROP_NOSUBS = "Nosubs";
	public static String PROP_FREQCODE = "Freqcode";
	public static String PROP_GCN_SEQNO = "GcnSeqno";
	public static String PROP_DOSAGE = "Dosage";
	public static String PROP_BN = "Bn";
	public static String PROP_ID = "Id";


	// constructors
	public BaseFavorites () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseFavorites (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseFavorites (
		java.lang.Integer id,
		java.lang.String providerNo,
		java.lang.String favoritename,
		java.math.BigDecimal gcnSeqno,
		boolean nosubs,
		boolean prn,
		java.lang.String special) {

		this.setId(id);
		this.setProviderNo(providerNo);
		this.setFavoritename(favoritename);
		this.setGcnSeqno(gcnSeqno);
		this.setNosubs(nosubs);
		this.setPrn(prn);
		this.setSpecial(special);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String providerNo;
	private java.lang.String favoritename;
	private java.lang.String bn;
	private java.math.BigDecimal gcnSeqno;
	private java.lang.String customName;
	private java.lang.Float takemin;
	private java.lang.Float takemax;
	private java.lang.String freqcode;
	private java.lang.String duration;
	private java.lang.String durunit;
	private java.lang.String quantity;
	private java.lang.Byte repeat;
	private boolean nosubs;
	private boolean prn;
	private java.lang.String special;
	private java.lang.String gn;
	private java.lang.String atc;
	private java.lang.String regionalIdentifier;
	private java.lang.String unit;
	private java.lang.String method;
	private java.lang.String route;
	private java.lang.String drugform;
	private java.lang.String dosage;
	private boolean customInstructions;
	private java.lang.String unitName;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="favoriteid"
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
	 * Return the value associated with the column: favoritename
	 */
	public java.lang.String getFavoritename () {
		return favoritename;
	}

	/**
	 * Set the value related to the column: favoritename
	 * @param favoritename the favoritename value
	 */
	public void setFavoritename (java.lang.String favoritename) {
		this.favoritename = favoritename;
	}



	/**
	 * Return the value associated with the column: BN
	 */
	public java.lang.String getBn () {
		return bn;
	}

	/**
	 * Set the value related to the column: BN
	 * @param bn the BN value
	 */
	public void setBn (java.lang.String bn) {
		this.bn = bn;
	}



	/**
	 * Return the value associated with the column: GCN_SEQNO
	 */
	public java.math.BigDecimal getGcnSeqno () {
		return gcnSeqno;
	}

	/**
	 * Set the value related to the column: GCN_SEQNO
	 * @param gcnSeqno the GCN_SEQNO value
	 */
	public void setGcnSeqno (java.math.BigDecimal gcnSeqno) {
		this.gcnSeqno = gcnSeqno;
	}



	/**
	 * Return the value associated with the column: customName
	 */
	public java.lang.String getCustomName () {
		return customName;
	}

	/**
	 * Set the value related to the column: customName
	 * @param customName the customName value
	 */
	public void setCustomName (java.lang.String customName) {
		this.customName = customName;
	}



	/**
	 * Return the value associated with the column: takemin
	 */
	public java.lang.Float getTakemin () {
		return takemin;
	}

	/**
	 * Set the value related to the column: takemin
	 * @param takemin the takemin value
	 */
	public void setTakemin (java.lang.Float takemin) {
		this.takemin = takemin;
	}



	/**
	 * Return the value associated with the column: takemax
	 */
	public java.lang.Float getTakemax () {
		return takemax;
	}

	/**
	 * Set the value related to the column: takemax
	 * @param takemax the takemax value
	 */
	public void setTakemax (java.lang.Float takemax) {
		this.takemax = takemax;
	}



	/**
	 * Return the value associated with the column: freqcode
	 */
	public java.lang.String getFreqcode () {
		return freqcode;
	}

	/**
	 * Set the value related to the column: freqcode
	 * @param freqcode the freqcode value
	 */
	public void setFreqcode (java.lang.String freqcode) {
		this.freqcode = freqcode;
	}



	/**
	 * Return the value associated with the column: duration
	 */
	public java.lang.String getDuration () {
		return duration;
	}

	/**
	 * Set the value related to the column: duration
	 * @param duration the duration value
	 */
	public void setDuration (java.lang.String duration) {
		this.duration = duration;
	}



	/**
	 * Return the value associated with the column: durunit
	 */
	public java.lang.String getDurunit () {
		return durunit;
	}

	/**
	 * Set the value related to the column: durunit
	 * @param durunit the durunit value
	 */
	public void setDurunit (java.lang.String durunit) {
		this.durunit = durunit;
	}



	/**
	 * Return the value associated with the column: quantity
	 */
	public java.lang.String getQuantity () {
		return quantity;
	}

	/**
	 * Set the value related to the column: quantity
	 * @param quantity the quantity value
	 */
	public void setQuantity (java.lang.String quantity) {
		this.quantity = quantity;
	}



	/**
	 * Return the value associated with the column: repeat
	 */
	public java.lang.Byte getRepeat () {
		return repeat;
	}

	/**
	 * Set the value related to the column: repeat
	 * @param repeat the repeat value
	 */
	public void setRepeat (java.lang.Byte repeat) {
		this.repeat = repeat;
	}



	/**
	 * Return the value associated with the column: nosubs
	 */
	public boolean isNosubs () {
		return nosubs;
	}

	/**
	 * Set the value related to the column: nosubs
	 * @param nosubs the nosubs value
	 */
	public void setNosubs (boolean nosubs) {
		this.nosubs = nosubs;
	}



	/**
	 * Return the value associated with the column: prn
	 */
	public boolean isPrn () {
		return prn;
	}

	/**
	 * Set the value related to the column: prn
	 * @param prn the prn value
	 */
	public void setPrn (boolean prn) {
		this.prn = prn;
	}



	/**
	 * Return the value associated with the column: special
	 */
	public java.lang.String getSpecial () {
		return special;
	}

	/**
	 * Set the value related to the column: special
	 * @param special the special value
	 */
	public void setSpecial (java.lang.String special) {
		this.special = special;
	}



	/**
	 * Return the value associated with the column: GN
	 */
	public java.lang.String getGn () {
		return gn;
	}

	/**
	 * Set the value related to the column: GN
	 * @param gn the GN value
	 */
	public void setGn (java.lang.String gn) {
		this.gn = gn;
	}



	/**
	 * Return the value associated with the column: ATC
	 */
	public java.lang.String getAtc () {
		return atc;
	}

	/**
	 * Set the value related to the column: ATC
	 * @param atc the ATC value
	 */
	public void setAtc (java.lang.String atc) {
		this.atc = atc;
	}



	/**
	 * Return the value associated with the column: regional_identifier
	 */
	public java.lang.String getRegionalIdentifier () {
		return regionalIdentifier;
	}

	/**
	 * Set the value related to the column: regional_identifier
	 * @param regionalIdentifier the regional_identifier value
	 */
	public void setRegionalIdentifier (java.lang.String regionalIdentifier) {
		this.regionalIdentifier = regionalIdentifier;
	}



	/**
	 * Return the value associated with the column: unit
	 */
	public java.lang.String getUnit () {
		return unit;
	}

	/**
	 * Set the value related to the column: unit
	 * @param unit the unit value
	 */
	public void setUnit (java.lang.String unit) {
		this.unit = unit;
	}



	/**
	 * Return the value associated with the column: method
	 */
	public java.lang.String getMethod () {
		return method;
	}

	/**
	 * Set the value related to the column: method
	 * @param method the method value
	 */
	public void setMethod (java.lang.String method) {
		this.method = method;
	}



	/**
	 * Return the value associated with the column: route
	 */
	public java.lang.String getRoute () {
		return route;
	}

	/**
	 * Set the value related to the column: route
	 * @param route the route value
	 */
	public void setRoute (java.lang.String route) {
		this.route = route;
	}



	public java.lang.String getDrugForm() {
		return drugform;
	}
	
	public void setDrugForm (java.lang.String drugform) {
		this.drugform = drugform;
	}
	
	/**
	 * Return the value associated with the column: dosage
	 */
	public java.lang.String getDosage () {
		return dosage;
	}

	/**
	 * Set the value related to the column: dosage
	 * @param dosage the dosage value
	 */
	public void setDosage (java.lang.String dosage) {
		this.dosage = dosage;
	}



	/**
	 * Return the value associated with the column: custom_instructions
	 */
	public boolean isCustomInstructions () {
		return customInstructions;
	}

	/**
	 * Set the value related to the column: custom_instructions
	 * @param customInstructions the custom_instructions value
	 */
	public void setCustomInstructions (boolean customInstructions) {
		this.customInstructions = customInstructions;
	}



	/**
	 * Return the value associated with the column: unitName
	 */
	public java.lang.String getUnitName () {
		return unitName;
	}

	/**
	 * Set the value related to the column: unitName
	 * @param unitName the unitName value
	 */
	public void setUnitName (java.lang.String unitName) {
		this.unitName = unitName;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.oscarRx.model.Favorites)) return false;
		else {
			oscar.oscarRx.model.Favorites favorites = (oscar.oscarRx.model.Favorites) obj;
			if (null == this.getId() || null == favorites.getId()) return false;
			else return (this.getId().equals(favorites.getId()));
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
