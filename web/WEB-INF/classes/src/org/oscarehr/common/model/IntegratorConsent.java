package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.oscarehr.caisi_integrator.ws.client.ConsentLevel;

@Entity
public class IntegratorConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;
    private int facilityId = 0;
    private int demographicId = 0;
    @Enumerated(EnumType.STRING)
    private ConsentLevel consentLevel = ConsentLevel.DO_NOT_SHARE;
    private String provider_no = null;
    @Version
    private Date lastUpdate = null;

    public int getId() {
        return id;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getDemographicId() {
        return demographicId;
    }

    public void setDemographicId(int demographicId) {
        this.demographicId = demographicId;
    }

    public ConsentLevel getConsentLevel() {
        return consentLevel;
    }

    public void setConsentLevel(ConsentLevel consentLevel) {
        this.consentLevel = consentLevel;
    }

    public String getProvider_no() {
        return provider_no;
    }

    public void setProvider_no(String provider_no) {
        this.provider_no = provider_no;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

}