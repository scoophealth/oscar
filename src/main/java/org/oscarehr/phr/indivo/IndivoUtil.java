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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.phr.indivo;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.indivo.xml.phr.contact.AddressType;
import org.indivo.xml.phr.contact.CommunicationType;
import org.indivo.xml.phr.contact.ContactInformationType;
import org.indivo.xml.phr.contact.NameType;
import org.indivo.xml.phr.demographics.DemographicsType;

import oscar.util.UtilDateUtilities;

/**
 *
 * @author apavel
 */
public class IndivoUtil {

public static ContactInformationType generateContactInformationType(
                                        String iFirstName,
                                        String iLastName,
                                        String iAddress,
                                        String iCity,
                                        String iProvince,
                                        String iPostal,
                                        String iPhone,
                                        String iPhone2,
                                        String iEmail) {
        
        org.indivo.xml.phr.contact.ObjectFactory cObjFactory = new org.indivo.xml.phr.contact.ObjectFactory();
        ContactInformationType indiContact = cObjFactory.createContactInformationType();
    
        //Name
        List<NameType> names = indiContact.getPersonName();
        NameType name = cObjFactory.createNameType();
        name.setFirstName(iFirstName);
        name.setLastName(iLastName);
        names.add(name);
        
        //Address
        List<AddressType> addresses = indiContact.getAddress();
        AddressType address = cObjFactory.createAddressType();
        address.setType("home");
        address.setPriority(new BigInteger("1"));
        address.setStreetAddress1(iAddress);
        address.setCity(iCity);
        address.setState(iProvince);
        address.setPostalCode(iPostal);
        addresses.add(address);
        
        //Contact - Email
        List<CommunicationType> comms = indiContact.getContactMethod();
        CommunicationType comm1 = cObjFactory.createCommunicationType();
        comm1.setMedium("email");
        comm1.setPriority(new BigInteger("1"));
        comm1.setClazz("default");
        comm1.setValue(iEmail);
        comms.add(comm1);
        
        //Contact - Home Phone
        CommunicationType comm2 = cObjFactory.createCommunicationType();
        comm2.setMedium("phone");
        comm2.setPriority(new BigInteger("1"));
        comm2.setClazz("home");
        comm2.setValue(iPhone);
        comms.add(comm2);
        
        //Contact - Work Phone
        CommunicationType comm3 = cObjFactory.createCommunicationType();
        comm3.setMedium("phone");
        comm3.setPriority(new BigInteger("1"));
        comm3.setClazz("daytime");
        comm3.setValue(iPhone2);
        comms.add(comm3);
        
        return indiContact;
    }

    public static DemographicsType generateDemographicType(String dob) throws DatatypeConfigurationException {
        org.indivo.xml.phr.demographics.ObjectFactory dObjFactory = new org.indivo.xml.phr.demographics.ObjectFactory();
        DemographicsType indiDemographic = dObjFactory.createDemographicsType();
        
        DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
        Date dobDate = UtilDateUtilities.StringToDate(dob, "yyyy/MM/dd");
        GregorianCalendar dobGregorian = new GregorianCalendar();
        dobGregorian.setTime(dobDate);
        XMLGregorianCalendar dobxml = dataTypeFactory.newXMLGregorianCalendar(dobGregorian);
        indiDemographic.setDateOfBirth(dobxml);
        
        return indiDemographic;
    }
    
}
