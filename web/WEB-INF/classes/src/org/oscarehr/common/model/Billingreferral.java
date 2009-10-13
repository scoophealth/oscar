/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Toby
 */
@Entity
@Table(name = "billingreferral")
@NamedQueries({@NamedQuery(name = "Billingreferral.findAll", query = "SELECT b FROM Billingreferral b")})
public class Billingreferral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "billingreferral_no")
    private Integer billingreferralNo;
    @Basic(optional = false)
    @Column(name = "referral_no")
    private String referralNo;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "specialty")
    private String specialty;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "city")
    private String city;
    @Column(name = "province")
    private String province;
    @Column(name = "country")
    private String country;
    @Column(name = "postal")
    private String postal;
    @Column(name = "phone")
    private String phone;
    @Column(name = "fax")
    private String fax;

    public Billingreferral() {
    }

    public Billingreferral(Integer billingreferralNo) {
        this.billingreferralNo = billingreferralNo;
    }

    public Billingreferral(Integer billingreferralNo, String referralNo) {
        this.billingreferralNo = billingreferralNo;
        this.referralNo = referralNo;
    }

    public Integer getBillingreferralNo() {
        return billingreferralNo;
    }

    public void setBillingreferralNo(Integer billingreferralNo) {
        this.billingreferralNo = billingreferralNo;
    }

    public String getReferralNo() {
        return referralNo;
    }

    public void setReferralNo(String referralNo) {
        this.referralNo = referralNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
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

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billingreferralNo != null ? billingreferralNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billingreferral)) {
            return false;
        }
        Billingreferral other = (Billingreferral) object;
        if ((this.billingreferralNo == null && other.billingreferralNo != null) || (this.billingreferralNo != null && !this.billingreferralNo.equals(other.billingreferralNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.oscarehr.common.model.Billingreferral[billingreferralNo=" + billingreferralNo + "]";
    }

}
