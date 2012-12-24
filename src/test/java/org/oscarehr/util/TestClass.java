/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.util;

import java.util.Date;

import javax.persistence.Column;

public class TestClass {

	@Column(name = "fancyColunmNameForDateObj")
	private Date dateObj;
	private Integer integerObj;
	private Boolean booleanObj;
	private Long longObj;
	private String stringObj;
	private Character characterObj;
	private Byte byteObj;
	private Short shortObj;
	private Float floatObj;
	private Double doubleObj;
	private int intPrim;
	private boolean booleanPrim;
	private long longPrim;
	private char charPrim;
	private byte bytePrim;
	private short shortPrim;
	private float floatPrim;
	private double doublePrim;

	public Date getDateObj() {
		return dateObj;
	}

	public void setDateObj(Date dateObj) {
		this.dateObj = dateObj;
	}

	public Integer getIntegerObj() {
		return integerObj;
	}

	public void setIntegerObj(Integer integerObj) {
		this.integerObj = integerObj;
	}

	public Boolean getBooleanObj() {
		return booleanObj;
	}

	public void setBooleanObj(Boolean booleanObj) {
		this.booleanObj = booleanObj;
	}

	public Long getLongObj() {
		return longObj;
	}

	public void setLongObj(Long longObj) {
		this.longObj = longObj;
	}

	public String getStringObj() {
		return stringObj;
	}

	public void setStringObj(String stringObj) {
		this.stringObj = stringObj;
	}

	public Character getCharacterObj() {
		return characterObj;
	}

	public void setCharacterObj(Character characterObj) {
		this.characterObj = characterObj;
	}

	public Byte getByteObj() {
		return byteObj;
	}

	public void setByteObj(Byte byteObj) {
		this.byteObj = byteObj;
	}

	public Short getShortObj() {
		return shortObj;
	}

	public void setShortObj(Short shortObj) {
		this.shortObj = shortObj;
	}

	public Float getFloatObj() {
		return floatObj;
	}

	public void setFloatObj(Float floatObj) {
		this.floatObj = floatObj;
	}

	public Double getDoubleObj() {
		return doubleObj;
	}

	public void setDoubleObj(Double doubleObj) {
		this.doubleObj = doubleObj;
	}

	public int getIntPrim() {
		return intPrim;
	}

	public void setIntPrim(int intPrim) {
		this.intPrim = intPrim;
	}

	public boolean isBooleanPrim() {
		return booleanPrim;
	}

	public void setBooleanPrim(boolean booleanPrim) {
		this.booleanPrim = booleanPrim;
	}

	public long getLongPrim() {
		return longPrim;
	}

	public void setLongPrim(long longPrim) {
		this.longPrim = longPrim;
	}

	public char getCharPrim() {
		return charPrim;
	}

	public void setCharPrim(char charPrim) {
		this.charPrim = charPrim;
	}

	public byte getBytePrim() {
		return bytePrim;
	}

	public void setBytePrim(byte bytePrim) {
		this.bytePrim = bytePrim;
	}

	public short getShortPrim() {
		return shortPrim;
	}

	public void setShortPrim(short shortPrim) {
		this.shortPrim = shortPrim;
	}

	public float getFloatPrim() {
		return floatPrim;
	}

	public void setFloatPrim(float floatPrim) {
		this.floatPrim = floatPrim;
	}

	public double getDoublePrim() {
		return doublePrim;
	}

	public void setDoublePrim(double doublePrim) {
		this.doublePrim = doublePrim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booleanObj == null) ? 0 : booleanObj.hashCode());
		result = prime * result + (booleanPrim ? 1231 : 1237);
		result = prime * result + ((byteObj == null) ? 0 : byteObj.hashCode());
		result = prime * result + bytePrim;
		result = prime * result + charPrim;
		result = prime * result + ((characterObj == null) ? 0 : characterObj.hashCode());
		result = prime * result + ((dateObj == null) ? 0 : dateObj.hashCode());
		result = prime * result + ((doubleObj == null) ? 0 : doubleObj.hashCode());
		long temp;
		temp = Double.doubleToLongBits(doublePrim);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((floatObj == null) ? 0 : floatObj.hashCode());
		result = prime * result + Float.floatToIntBits(floatPrim);
		result = prime * result + intPrim;
		result = prime * result + ((integerObj == null) ? 0 : integerObj.hashCode());
		result = prime * result + ((longObj == null) ? 0 : longObj.hashCode());
		result = prime * result + (int) (longPrim ^ (longPrim >>> 32));
		result = prime * result + ((shortObj == null) ? 0 : shortObj.hashCode());
		result = prime * result + shortPrim;
		result = prime * result + ((stringObj == null) ? 0 : stringObj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TestClass other = (TestClass) obj;
		if (booleanObj == null) {
			if (other.booleanObj != null) return false;
		} else if (!booleanObj.equals(other.booleanObj)) return false;
		if (booleanPrim != other.booleanPrim) return false;
		if (byteObj == null) {
			if (other.byteObj != null) return false;
		} else if (!byteObj.equals(other.byteObj)) return false;
		if (bytePrim != other.bytePrim) return false;
		if (charPrim != other.charPrim) return false;
		if (characterObj == null) {
			if (other.characterObj != null) return false;
		} else if (!characterObj.equals(other.characterObj)) return false;
		if (dateObj == null) {
			if (other.dateObj != null) return false;
		} else if (!dateObj.equals(other.dateObj)) return false;
		if (doubleObj == null) {
			if (other.doubleObj != null) return false;
		} else if (!doubleObj.equals(other.doubleObj)) return false;
		if (Double.doubleToLongBits(doublePrim) != Double.doubleToLongBits(other.doublePrim)) return false;
		if (floatObj == null) {
			if (other.floatObj != null) return false;
		} else if (!floatObj.equals(other.floatObj)) return false;
		if (Float.floatToIntBits(floatPrim) != Float.floatToIntBits(other.floatPrim)) return false;
		if (intPrim != other.intPrim) return false;
		if (integerObj == null) {
			if (other.integerObj != null) return false;
		} else if (!integerObj.equals(other.integerObj)) return false;
		if (longObj == null) {
			if (other.longObj != null) return false;
		} else if (!longObj.equals(other.longObj)) return false;
		if (longPrim != other.longPrim) return false;
		if (shortObj == null) {
			if (other.shortObj != null) return false;
		} else if (!shortObj.equals(other.shortObj)) return false;
		if (shortPrim != other.shortPrim) return false;
		if (stringObj == null) {
			if (other.stringObj != null) return false;
		} else if (!stringObj.equals(other.stringObj)) return false;
		return true;
	}

	@Override
    public String toString() {
	    return "TestClass [dateObj=" + dateObj + ", integerObj=" + integerObj + ", booleanObj=" + booleanObj + ", longObj=" + longObj + ", stringObj=" + stringObj + ", characterObj=" + characterObj + ", byteObj=" + byteObj + ", shortObj=" + shortObj + ", floatObj=" + floatObj + ", doubleObj=" + doubleObj + ", intPrim=" + intPrim + ", booleanPrim=" + booleanPrim + ", longPrim=" + longPrim + ", charPrim=" + charPrim + ", bytePrim=" + bytePrim + ", shortPrim=" + shortPrim + ", floatPrim=" + floatPrim
	            + ", doublePrim=" + doublePrim + "]";
    }

}
