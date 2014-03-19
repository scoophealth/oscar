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
package org.oscarehr.sharingcenter.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.*;

import org.marc.shic.core.configuration.IheActorConfiguration;
import org.marc.shic.core.configuration.IheActorType;
import org.marc.shic.core.configuration.IheIdentification;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "sharing_actor")
public class ActorDataObject extends AbstractModel<Integer> implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "oid")
    public String oid;

    @Column(name = "name")
    public String name;

    //TODO: Enumerate this?
    @Column(name = "actor_type")
    public String actorType;

    @Column(name = "secure", nullable = false, columnDefinition = "TINYINT(1)")
    public Boolean secure;

	//@Column(name="receiving_application")
    //public String receivingApplication;
    //
    //@Column(name="receiving_facility")
    //public String receivingFacility;
    @Column(name = "endpoint")
    public String endpoint;

    @ManyToOne
    @JoinColumn(name = "domain_fk")
    public AffinityDomainDataObject affinityDomain;

    @Embedded
    IdentificationDataObject remoteIdentification;

    public ActorDataObject() {

    }

    public ActorDataObject(String oid, String name, String actorType, Boolean isSecure, String endpoint, String receivingApplication, String receivingFacility) {
        this.oid = oid;
        this.name = name;
        this.actorType = actorType;
        this.secure = isSecure;
        this.endpoint = endpoint;
        this.remoteIdentification = new IdentificationDataObject(receivingApplication, receivingFacility);
    }

    public ActorDataObject(String oid, String name, String actorType, Boolean isSecure, String endpoint, IdentificationDataObject identification) {
        this.oid = oid;
        this.name = name;
        this.actorType = actorType;
        this.secure = isSecure;
        this.endpoint = endpoint;
        this.remoteIdentification = identification;
    }

    public ActorDataObject(IheActorConfiguration iheActor) {
        this.oid = iheActor.getOid();
        this.name = iheActor.getName();
        this.actorType = iheActor.getActorType().toString();
        this.secure = iheActor.isSecure();
        this.remoteIdentification = new IdentificationDataObject(iheActor.getRemoteIdentification().getApplicationId(), iheActor.getRemoteIdentification().getFacilityName());
        this.endpoint = iheActor.getEndPointAddress().toString();
    }

    public Integer getId() {
        return id;
    }

    /**
     * @return the oid
     */
    public String getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the actorType
     */
    public IheActorType getActorType() {
        return IheActorType.fromString(actorType);
    }

    /**
     * @param actorType the actorType to set
     */
    public void setActorType(IheActorType actorType) {
        this.actorType = actorType.toString();
    }

    /**
     * @return the secure
     */
    public Boolean getSecure() {
        return secure;
    }

    /**
     * @param secure the secure to set
     */
    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the affinityDomain
     */
    public AffinityDomainDataObject getAffinityDomain() {
        return affinityDomain;
    }

    /**
     * @param affinityDomain the affinityDomain to set
     */
    public void setAffinityDomain(AffinityDomainDataObject affinityDomain) {
        this.affinityDomain = affinityDomain;
    }

    /**
     * @return the remoteIdentification
     */
    public IdentificationDataObject getRemoteIdentification() {
        return remoteIdentification;
    }

    /**
     * @param remoteIdentification the remoteIdentification to set
     */
    public void setRemoteIdentification(IdentificationDataObject remoteIdentification) {
        this.remoteIdentification = remoteIdentification;
    }

    @Embeddable
    public static class IdentificationDataObject {

        @Column(name = "id_facility_name")
        String facilityName;

        @Column(name = "id_application_id")
        String applicationId;

        public IdentificationDataObject() {

        }

        public IdentificationDataObject(String applicationId, String facilityName) {
            this.facilityName = facilityName;
            this.applicationId = applicationId;
        }

        public IheIdentification createIheIdentification() {
            return new IheIdentification(this.applicationId, this.facilityName);
        }

        public String getFacilityName() {
            return facilityName;
        }

        public void setFacilityName(String facilityName) {
            this.facilityName = facilityName;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }

    }

    public IheActorConfiguration createIheActorConfiguration() {
        IheActorConfiguration a = new IheActorConfiguration();

        a.setName(name);
        a.setOid(oid);
        a.setSecure(secure);
        a.setActorType(this.getActorType());
        a.setRemoteIdentification(remoteIdentification.createIheIdentification());
        try {
            a.setEndPointAddress(new URI(endpoint));
        } catch (URISyntaxException e) {

        }

        return a;

    }

}
