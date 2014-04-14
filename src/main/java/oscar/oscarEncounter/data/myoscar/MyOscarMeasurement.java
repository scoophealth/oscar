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
package oscar.oscarEncounter.data.myoscar;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.common.model.Measurement;

import oscar.util.ConversionUtils;

public abstract class MyOscarMeasurement implements Serializable, Comparable<MyOscarMeasurement> {

    private static final long serialVersionUID = 1L;
	
    private Measurement measurement;
	
	public Date getDate() {
		return measurement.getDateObserved();
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
	
	protected Element parse(String value) {
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
	        document = builder.build(new StringReader(value));
        } catch (Exception e) {
        	return null;
        }
		return document.getRootElement();
	}
	
	@Override
    public int compareTo(MyOscarMeasurement o) {
	    return getDate().compareTo(o.getDate());
    }
	
	String getValue(String measurementType) {
		Element root = getRoot();
		if (root == null) {
			return null;
		}
		return root.getChildText(measurementType);
	}
	
	public boolean getSentFromPhr() {
		return isSentFromPhr();
	}
	
	public boolean isSentFromPhr() {
		return getMeasurement().getProviderNo() != null || getMeasurement().getAppointmentNo() != null;
	}
	
	String getUnits(String measurementType) {
		Element root = getRoot();
		if (root == null) {
			return null;
		}
		
		return root.getChildText("Units");
	}
	
	/**
	 * Converts the specified XML for a measurement into text that 
	 * can be displayed to the end user. 
	 * 
	 * @param element
	 * 		Root XML element returned by MyOSCAR
	 * @return
	 * 		Returns the text representation of this element.
	 */
	protected abstract String toReadableString(Element element);

	protected Element getRoot() {
		if (getMeasurement() == null) {
			return null;
		}

		Element root = parse(getMeasurement().getDataField());
		return root;
	}
	
	@Override
    public String toString() {
		Element root = getRoot();
		if (root == null) {
			return getMeasurement().getDataField();
		}

	    return toReadableString(root);
    }
	

	public String getComments() {
		return getMeasurement().getComments();
	}

	public int compareTo(String measurementType, String value) {
	    Double thisValue = ConversionUtils.fromDoubleString(getValue(measurementType));
	    Double thatValue = ConversionUtils.fromDoubleString(value);
	    return thisValue.compareTo(thatValue);
    }

	public String add(String measurementType, String value) {
	    Double thisValue = ConversionUtils.fromDoubleString(getValue(measurementType));
	    Double thatValue = ConversionUtils.fromDoubleString(value);
	    
	    return ConversionUtils.toDoubleString(thisValue.doubleValue() + thatValue.doubleValue());
    }

	public String getMeasurementValueByType(String type) {
		Element root = getRoot();
		return root.getChildText(type);
	}

}
