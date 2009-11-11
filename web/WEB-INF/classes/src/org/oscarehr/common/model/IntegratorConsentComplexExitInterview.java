package org.oscarehr.common.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class IntegratorConsentComplexExitInterview extends AbstractModel<FacilityDemographicPrimaryKey> {
	@EmbeddedId
	private FacilityDemographicPrimaryKey id;

	private String spokenLanguage = null;
	private String readLanguage = null;
	private String education = null;
	private String timeToReviewConsent = null;
	private String timeToReviewConsentComments = null;
	private String pressured = null;
	private String pressuredComments = null;
	private String moreInfo = null;
	private String moreInfoComments = null;
	private String reAskConsent = null;
	private String reAskConsentComments = null;
	private String additionalComments = null;

	public FacilityDemographicPrimaryKey getId() {
		return id;
	}

	public void setId(FacilityDemographicPrimaryKey id) {
		this.id = id;
	}

	public String getSpokenLanguage() {
		return spokenLanguage;
	}

	public void setSpokenLanguage(String spokenLanguage) {
		this.spokenLanguage = spokenLanguage;
	}

	public String getReadLanguage() {
		return readLanguage;
	}

	public void setReadLanguage(String readLanguage) {
		this.readLanguage = readLanguage;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getTimeToReviewConsent() {
		return timeToReviewConsent;
	}

	public void setTimeToReviewConsent(String timeToReviewConsent) {
		this.timeToReviewConsent = timeToReviewConsent;
	}

	public String getTimeToReviewConsentComments() {
		return timeToReviewConsentComments;
	}

	public void setTimeToReviewConsentComments(
			String timeToReviewConsentComments) {
		this.timeToReviewConsentComments = timeToReviewConsentComments;
	}

	public String getPressured() {
		return pressured;
	}

	public void setPressured(String pressured) {
		this.pressured = pressured;
	}

	public String getPressuredComments() {
		return pressuredComments;
	}

	public void setPressuredComments(String pressuredComments) {
		this.pressuredComments = pressuredComments;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public String getMoreInfoComments() {
		return moreInfoComments;
	}

	public void setMoreInfoComments(String moreInfoComments) {
		this.moreInfoComments = moreInfoComments;
	}

	public String getReAskConsent() {
		return reAskConsent;
	}

	public void setReAskConsent(String reAskConsent) {
		this.reAskConsent = reAskConsent;
	}

	public String getReAskConsentComments() {
		return reAskConsentComments;
	}

	public void setReAskConsentComments(String reAskConsentComments) {
		this.reAskConsentComments = reAskConsentComments;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

}
