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

package org.oscarehr.integration.cdx.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "cdx_person")
public class CdxPerson extends AbstractModel<Integer> implements Serializable {

    public static final String rolePatient = "PAT";
    public static final String roleAuthor = "AUT";
    public static final String rolePrimaryRecipient = "PRI";
    public static final String roleSecondaryRecipient = "SEC";
    public static final String roleOrderingProvider = "OP";
    public static final String roleFamilyProvider = "FP";
    public static final String roleParticipatingProvider = "PP";
    public static final String roleProcedurePerformer = "PER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "birthdate")
    private Date birthdate;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "city")
    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "province")
    private String province;
    @Column(name = "country")
    private String country;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "clinic_id")
    private String clinicId;
    @Column(name = "clinic_name")
    private String clinicName;
    @Column(name = "document")
    private int document;
    @Column(name = "role_in_doc")
    private String roleInDocument;

    public CdxPerson() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer internalId) {
        this.id = internalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrefix() {
        return (prefix == null ? "" : prefix);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public String getRoleInDocument() {
        return roleInDocument;
    }

    public void setRoleInDocument(String roleInDocument) {
        this.roleInDocument = roleInDocument;
    }

    public String getFullProviderName() {
        String result = "";

        if (!prefix.equals(""))
            result = prefix + " ";

        result = result + firstName + " " + lastName;

        if (!clinicName.equals(""))
            result = result + ", " + clinicName;

        return result;
    }
}