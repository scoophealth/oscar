/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.util.Date;
import java.util.zip.CRC32;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.integration.mchcv.HCValidationFactory;
import org.oscarehr.integration.mchcv.HCValidationResult;
import org.oscarehr.integration.mchcv.HCValidator;

/**
 * This object is to help support tracking which fields in the demographic object have been marked as "valid" by an end user. The original intent is because only "validated" information is allowed to be sent to the HNR and part of the scope of work was to
 * track who validated which fields and when. To keep track of what is validated and to help nullify the validation upon change of data, we're going to use a CRC32 on the data being validated. The reason is because there's currently no reliable way of
 * hooking into when some of this data is updated so short of making a copy of all the data when validation occurs... this seemed like the only other reasonable alternative. I know a CRC isn't fool proof but it should be "good enough" in this case. <br />
 * <br />
 * Note it takes 2 bits and 2 checks to see if something is valid. 1) the valid=trues 2) the crc must be correct for your current data. <br />
 * <br />
 * The reason it takes 2 is because, 1) we can't allow changes to the valid bit after it's set for auditing purposes, we need to know who set it to what. anyone who wants to change anything must create a new record. 2) the crc is to track if the validated
 * data changed. i.e. if I say the picture is valid, then some one uploads a new one, I need to be able to say only the previous one was caled valid, this one is no longer valid until some one validates it too.
 */
@Entity
public class HnrDataValidation extends AbstractModel<Integer> {

	private static final char RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR = '\u009C';

	public enum Type {
		PICTURE, HC_INFO, OTHER
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	private Integer facilityId = null;
	private Integer clientId = null;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = null;
	/** this is the provider id who validated this piece of data */
	private String validatorProviderNo = null;
	private boolean valid = false;
	@Enumerated(EnumType.STRING)
	private Type validationType = null;
	private long validationCrc = -1;

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getValidatorProviderNo() {
		return validatorProviderNo;
	}

	public void setValidatorProviderNo(String validatorProviderNo) {
		this.validatorProviderNo = validatorProviderNo;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
    public Integer getId() {
		return id;
	}

	public Type getValidationType() {
		return validationType;
	}

	public void setValidationType(Type validationType) {
		this.validationType = validationType;
	}

	public long getValidationCrc() {
		return validationCrc;
	}

	public void setValidationCrc(long validationCrc) {
		this.validationCrc = validationCrc;
	}

	public void setValidationCrc(byte[] b) {
		if (b != null) {
			CRC32 crc32 = new CRC32();
			crc32.update(b);
			setValidationCrc(crc32.getValue());
		} else {
			setValidationCrc(-1L);
		}
	}

	/**
	 * @return false if b==null
	 */
	public boolean isMatchingCrc(byte[] b) {
		if (b == null) return (false);

		CRC32 crc32 = new CRC32();
		crc32.update(b);

		return (validationCrc!=-1 && crc32.getValue()==validationCrc);
	}

	public boolean isValidAndMatchingCrc(byte[] b) {
		return (isValid() && isMatchingCrc(b));
	}

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

	public static boolean isImageValidated(ClientImage clientImage) {
		if (clientImage == null || clientImage.getImage_data()==null) return(false);
		else return(true);
	}
	
	public static byte[] getImageValidationBytes(ClientImage clientImage) {
		if (!isImageValidated(clientImage)) return(null);
			
		return(clientImage.getImage_data());
	}
	
	public static boolean isHcInfoValidateable(Demographic demographic) {
		if (demographic == null) return false;
		if (demographic.getFirstName() == null) return false;
		if (demographic.getLastName() == null) return false;
		if (demographic.getSex() == null) return false;
		if (demographic.getBirthDay()==null) return false;
		if (demographic.getHin() == null) return false;
                if (demographic.getHcType() == null) return false;
                if (demographic.getEffDate() == null) return false;
                if (demographic.getHcRenewDate() == null) return false;
                
                HCValidator validator = HCValidationFactory.getHCValidator();
                HCValidationResult validationResult = validator.validate(demographic.getHin(), demographic.getHcType().toLowerCase());
                boolean hinValid = validationResult.isValid();
                if (!hinValid) return false;
                
		return true;
	}
	
	public static byte[] getHcInfoValidationBytes(Demographic demographic) {
		if (!isHcInfoValidateable(demographic)) return(null);
		
		StringBuilder sb = new StringBuilder();

		sb.append(demographic.getFirstName()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getLastName()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getSex()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getFormattedDob()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getHin()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		if (demographic.getVer()!=null) sb.append(demographic.getVer()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getHcType()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getEffDate()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getHcRenewDate()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);

		return (sb.toString().getBytes());
	}

	public static boolean isOtherInfoValidateable(Demographic demographic) {
		if (demographic==null) return(false);
		else if (demographic.getAddress()==null) return(false);
		else if (demographic.getCity()==null) return(false);
		else if (demographic.getProvince()==null) return(false);
		else return(true);
		
	}
	
	public static byte[] getOtherInfoValidationBytes(Demographic demographic) {
		if (!isOtherInfoValidateable(demographic)) return(null);
		
		StringBuilder sb = new StringBuilder();

		sb.append(demographic.getAddress()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getCity()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);
		sb.append(demographic.getProvince()).append(RANDOM_NON_PRINTABLE_CRC_SEPARATOR_CHAR);

		return (sb.toString().getBytes());
	}
}
