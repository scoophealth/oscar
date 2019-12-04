/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.datatypes.TelcoType;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.receive.ITelco;
import ca.uvic.leadlab.obibconnector.facades.registry.IClinic;
import ca.uvic.leadlab.obibconnector.facades.registry.IProvider;
import ca.uvic.leadlab.obibconnector.facades.registry.ISearchProviders;
import ca.uvic.leadlab.obibconnector.impl.registry.SearchProviders;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.integration.cdx.dao.CdxClinicsDao;
import org.oscarehr.integration.cdx.model.CdxClinics;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.util.*;

public class CDXSpecialist {
    private final CDXConfiguration config;

    public CDXSpecialist() {
        config = new CDXConfiguration();
    }

    public List<IProvider> findAll() throws OBIBException {
        List<IProvider> allProviders = new ArrayList<IProvider>();

        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            String s = ""+alphabet;
            ISearchProviders searchProviders = new SearchProviders(config);
            List<IProvider> providers = searchProviders.findByName(s);
            if (providers != null) {
                for (IProvider p : providers) {
                    if (p.getLastName().toUpperCase().startsWith(s) && !allProviders.contains(p)) {
                        allProviders.add(p);
                    }
                }
            }
        }
        return allProviders;
    }

    public List<IProvider> findAllNoException() {
        List<IProvider> allProviders = new ArrayList<IProvider>();
        try {
            allProviders = findAll();
        } catch (OBIBException e) {
            MiscUtils.getLogger().error(e.getMessage());
        }
        Collections.sort(allProviders, IProviderComparator);
        return allProviders;
    }

    public List<IProvider> findCdxSpecialistByName(String name) {

        if (name == null || "".equals(name.trim())) {
            return findAllNoException();
        }

        name = name.trim();

        List<IProvider> providers = new ArrayList<IProvider>();

        try {
            ISearchProviders searchProviders = new SearchProviders(config);
            providers = searchProviders.findByName(name);
            Collections.sort(providers, IProviderComparator);
        } catch (OBIBException e) {
            MiscUtils.getLogger().info("findByName('"+name+"') returned: " + e.getMessage());
            //MiscUtils.getLogger().error("Searching for CDX specialist by name failed", e);
        }
        return providers;
    }

    public List<IProvider> findCdxSpecialistByLastName(String lastName) {

        if (lastName == null || "".equals(lastName.trim())) {
            return null;
        }
        lastName = lastName.trim();

        List<IProvider> providers;
        List<IProvider> result = new ArrayList<IProvider>();

        try {
            ISearchProviders searchProviders = new SearchProviders(config);
            providers = searchProviders.findByName(lastName);

            for (IProvider p : providers) {
                if (p.getLastName().startsWith(lastName)) {
                    result.add(p);
                }
            }

        } catch (OBIBException e) {
            MiscUtils.getLogger().error("Searching for CDX specialist by last name failed", e);
        }
        return result;
    }

    public List<IProvider> findCdxSpecialistByFirstName(String firstName) {

        if (firstName == null || "".equals(firstName.trim())) {
            return null;
        }
        firstName = firstName.trim();

        List<IProvider> providers;
        List<IProvider> result = new ArrayList<IProvider>();

        try {
            ISearchProviders searchProviders = new SearchProviders(config);
            providers = searchProviders.findByName(firstName);

            for (IProvider p : providers) {
                if (p.getFirstName().startsWith(firstName)) {
                    result.add(p);
                }
            }

        } catch (OBIBException e) {
            MiscUtils.getLogger().error("Searching for CDX specialist by first name failed", e);
        }
        return result;
    }

    public List<IProvider> findCdxSpecialistById(String id) {

        if (id == null || "".equals(id.trim())) {
            return null;
        }
        new NotificationController().deleteNotifications("specialist");
        List<IProvider> providers;
        List<IProvider> result = new ArrayList<IProvider>();

        try {
            ISearchProviders searchProviders = new SearchProviders(config);
            providers = searchProviders.findByProviderID(id);

            for (IProvider p : providers) {
                if (id.equalsIgnoreCase(p.getID())) {
                    result.add(p);
                }
            }

        } catch (OBIBException e) {
            new NotificationController().insertNotifications("Warning","Searching for CDX specialist by ID failed for ID [" + id + "] "+e.getObibMessage(),"specialist");
            MiscUtils.getLogger().warn("Searching for CDX specialist by ID failed for ID [" + id + "]");
        }
        return result;
    }


    public Set<CdxClinics> saveCdxClinics( IProvider p) {
        Set<CdxClinics> cdxClinics = new HashSet<>();
        String address = "";
        CdxClinicsDao cdxClinicsDao = SpringUtils.getBean(CdxClinicsDao.class);


        for (IClinic clinic : p.getClinics()) {

            CdxClinics dbClinic = cdxClinicsDao.findByClinicId(clinic.getID());
            if (dbClinic != null) {
                cdxClinics.add(dbClinic);
            } else {

                if (clinic.getID() != null && !clinic.getID().isEmpty()) {
                    CdxClinics cdxClinic = new CdxClinics();
                    cdxClinic.setClinicId(clinic.getID().trim());

                    if (clinic.getName() != null && !clinic.getName().isEmpty()) {
                        cdxClinic.setClinicName(clinic.getName().trim());
                    }
                    if (clinic.getStreetAddress() != null && !clinic.getStreetAddress().isEmpty()) {
                        address = clinic.getStreetAddress();
                    }
                    if (clinic.getCity() != null && !clinic.getCity().isEmpty()) {
                        address = address + " " + clinic.getCity();
                    }
                    if (clinic.getProvince() != null && !clinic.getProvince().isEmpty()) {
                        address = address + " " + clinic.getProvince();
                    }
                    if (clinic.getPostalCode() != null && !clinic.getPostalCode().isEmpty()) {
                        address = address + " " + clinic.getPostalCode();
                    }
                    cdxClinic.setClinicAddress(address);
                    cdxClinics.add(cdxClinic);
                    try {
                        cdxClinicsDao.persist(cdxClinic);
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("Got exception saving Clinic Information " + e.getMessage());
                    }

                }
            }

            }

            return cdxClinics;
        }



    public Boolean saveProfessionalSpecialist(String cdxSpecId) {

        boolean result = false;

        ProfessionalSpecialistDao professionalSpecialistDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
        List<ProfessionalSpecialist> professionalSpecialistList = professionalSpecialistDao.findByCdxId(cdxSpecId);

        List<IProvider> providers = findCdxSpecialistById(cdxSpecId);
        if (providers != null && !providers.isEmpty()) {
            IProvider p = providers.get(0);

            if (professionalSpecialistList != null && professionalSpecialistList.size() != 0) {
                professionalSpecialistList.get(0).setClinics(saveCdxClinics(p));
                professionalSpecialistDao.merge(professionalSpecialistList.get(0));
                result =true;
              }
            else
                {
                 ProfessionalSpecialist professionalSpecialist = new ProfessionalSpecialist();
                 ArrayList<String> address = new ArrayList<String>();
                 String annotations = null;
                 String comma = " ,";
                 String tmpStr;
                 String emailW = null;
                 String emailH = null;
                 String emailM = null;

            professionalSpecialist.setLastName(p.getLastName());
            professionalSpecialist.setFirstName(p.getFirstName());
            professionalSpecialist.setProfessionalLetters(p.getPrefix());
            professionalSpecialist.setSalutation(p.getPrefix());
            if (p.getClinicName() != null && !p.getClinicName().trim().isEmpty()) {
                address.add(p.getClinicName().trim());
            }
            if (p.getStreetAddress() != null && !p.getStreetAddress().trim().isEmpty()) {
                address.add(p.getStreetAddress().trim());
            }
            String[] addressArr = new String[address.size()];
            for (int j = 0; j < address.size(); j++) {
                addressArr[j] = address.get(j);
            }
            professionalSpecialist.setStreetAddress(addressArr);
            professionalSpecialist.setCity(p.getCity());
            professionalSpecialist.setProvince(p.getProvince());
            professionalSpecialist.setPostal(p.getPostalCode());
            // Organization name overwrites first name!!!
            // professionalSpecialist.setOrganizationName(p.getClinicName());

//            if (p.getClinicName() != null && !p.getClinicName().isEmpty()) {
//                tmpStr = "Clinic Name: " + p.getClinicName();
//                if (annotations == null) {
//                    annotations = tmpStr;
//                } else {
//                    annotations += comma + tmpStr;
//                }
//            }
//            if (p.getClinicID() != null && !p.getClinicID().isEmpty()) {
//                tmpStr = "Clinic ID: " + p.getClinicID();
//                if (annotations == null) {
//                    annotations = tmpStr;
//                } else {
//                    annotations += comma + tmpStr;
//                }
//            }

            professionalSpecialist.setAnnotation(p.getClinicName() + " (" + p.getClinicID() + ")");
            List<ITelco> phones = p.getPhones();
            for (ITelco phone : phones) {
                if (TelcoType.WORK.equals(phone.getTelcoType())) {
                    professionalSpecialist.setWorkPhone(phone.getAddress());
                } else if (TelcoType.HOME.equals(phone.getTelcoType())) {
                    professionalSpecialist.setPrivatePhoneNumber(phone.getAddress());
                } else if (TelcoType.MOBILE.equals((phone.getTelcoType()))) {
                    professionalSpecialist.setCellPhoneNumber(phone.getAddress());
                }
            }
            List<ITelco> emails = p.getEmails();
            //professionalSpecialists has one email field,
            //prefer work email, mobile email, home email in that order
            for (ITelco email : emails) {
                if (TelcoType.WORK.equals(email.getTelcoType())) {
                    //professionalSpecialist.setEmailAddress(email.getAddress());
                    emailW = email.getAddress().trim();
                } else if (TelcoType.MOBILE.equals((email.getTelcoType()))) {
//                    tmpStr = "Email(M): " + email.getAddress();
//                    if (annotations == null) {
//                        annotations = tmpStr;
//                    } else {
//                        annotations += comma + tmpStr;
//                    }
                    emailM = email.getAddress().trim();
                } else if (TelcoType.HOME.equals(email.getTelcoType())) {
//                    tmpStr = "Email(H): " + email.getAddress();
//                    if (annotations == null) {
//                        annotations = tmpStr;
//                    } else {
//                        annotations += comma + tmpStr;
//                    }
                    emailH = email.getAddress().trim();
                }
            }
            if (emailW != null && !emailW.isEmpty()) {
                professionalSpecialist.setEmailAddress(emailW);
            } else if (emailM != null && !emailM.isEmpty()) {
                professionalSpecialist.setEmailAddress(emailM);
            } else if (emailH != null && !emailH.isEmpty()) {
                professionalSpecialist.setEmailAddress(emailH);
            }
            professionalSpecialist.setCdxCapable(true);
            professionalSpecialist.setCdxId(cdxSpecId);
            professionalSpecialist.setAnnotation(annotations);

            try {
                //Calling saveCdxClinics method to store all the clinics related to the specialist.
                professionalSpecialist.setClinics(saveCdxClinics(p));
                professionalSpecialistDao.persist(professionalSpecialist);
                result = true;

            } catch (Exception e) {
                MiscUtils.getLogger().error("Got exception saving professional specialist: " + e.getMessage());
            }
        }
        }
        return result;
    }

    public String providerDescription(String cdxSpecId) {
        List<IProvider> providers = findCdxSpecialistById(cdxSpecId);
        if (providers != null && !providers.isEmpty()) {
            return providerDescription(providers.get(0));
        } else {
            return null;
        }
    }

    public String providerDescription(IProvider provider) {
        List<ITelco> phones = provider.getPhones();
        List<ITelco> emails = provider.getEmails();
        String workPhone = null;
        String homePhone = null;
        String cellPhone = null;
        if (phones != null && !phones.isEmpty()) {
            for (ITelco p : phones) {
                if (p.getTelcoType().equals(TelcoType.WORK)) {
                    workPhone = p.getAddress();
                }
                if (p.getTelcoType().equals(TelcoType.HOME)) {
                    homePhone = p.getAddress();
                }
                if (p.getTelcoType().equals((TelcoType.MOBILE))) {
                    cellPhone = p.getAddress();
                }
            }
        }
        String phoneStr = "Phone: ";
        if (workPhone != null) {
            phoneStr += " (W) " + workPhone;
        }
        if (homePhone != null) {
            phoneStr += " (H) " + homePhone;
        }
        if (cellPhone != null) {
            phoneStr += " (C) " + cellPhone;
        }

        String workEmail = null;
        String homeEmail = null;
        String otherEmail = null;
        if (emails != null && !emails.isEmpty()) {
            for (ITelco e : emails) {
                if (e.getTelcoType().equals(TelcoType.WORK)) {
                    workEmail = e.getAddress();
                }
                if (e.getTelcoType().equals(TelcoType.HOME)) {
                    homeEmail = e.getAddress();
                }
                if (e.getTelcoType().equals((TelcoType.MOBILE))) {
                    otherEmail = e.getAddress();
                }
            }
        }
        String email = "Email: ";
        if (workEmail != null) {
            email += " (W) " + workEmail;
        }
        if (homeEmail != null) {
            email += " (H) " + homeEmail;
        }
        if (otherEmail != null) {
            email += " (Other) " + homeEmail;
        }
        String clinicsIds="";
        String clinicsNames="";

        for(IClinic clinic : provider.getClinics()) {
            if(clinic.getID()!=null && !clinic.getID().isEmpty()){
                clinicsIds= clinicsIds+clinic.getID()+", ";
            }
            if(clinic.getName()!=null && !clinic.getName().isEmpty()){
                clinicsNames= clinicsNames+clinic.getName()+", ";
            }
        }


        String nl = System.lineSeparator();
        return nl + "ID: " + provider.getID() + nl +
                "First Name: " + provider.getFirstName() + nl +
                "Last Name: " + provider.getLastName() + nl +
                "Prefix: " + provider.getPrefix() + nl +
                "Street Address: " + provider.getStreetAddress() + nl +
                "City: " + provider.getCity() + nl +
                "Province: " + provider.getProvince() + nl +
                "Postal Code: " + provider.getPostalCode() + nl +
                "Country: " + provider.getCountry() + nl +
                phoneStr + nl +
                email + nl +
                "Clinic Ids: " + clinicsIds+ nl +
                "Clinic Names: " + clinicsNames + nl;
    }

    private final Comparator<IProvider> IProviderComparator = new Comparator<IProvider>() {

        public int compare(IProvider p1, IProvider p2) {
            String lName1 = p1.getLastName().toUpperCase();
            String fName1 = p1.getFirstName().toUpperCase();
            String cdxId1 = p1.getID();
            String lName2 = p2.getLastName().toUpperCase();
            String fName2 = p2.getFirstName().toUpperCase();
            String cdxId2 = p2.getID();
            if (lName1.equals(lName2) && fName1.equals(fName2)) {
                return cdxId1.compareTo(cdxId2);
            } else if (lName1.equalsIgnoreCase(lName2)) {
                return fName1.compareTo(fName2);
            } else {
                return lName1.compareTo(lName2);
            }
        }

    };
}
