package org.oscarehr.PMmodule.web;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.util.MiscUtils;

public class ReferralHistoryDisplay {
	private static final Logger logger=MiscUtils.getLogger();
	
	public static final Comparator<ReferralHistoryDisplay> REFERRAL_DATE_COMPARATOR=new Comparator<ReferralHistoryDisplay>() {
		public int compare(ReferralHistoryDisplay arg0, ReferralHistoryDisplay arg1) {
			return(arg1.referralDate.compareTo(arg0.referralDate));
		}
	};

	private int id;
	private String destinationProgramName;
	private String destinationProgramType;
	private Date referralDate;
	private Date completionDate;
	private String sourceProgramName;
	private String external;
	private boolean isRemoteReferral;

	public ReferralHistoryDisplay(ClientReferral clientReferral) {
		isRemoteReferral = false;
		id = clientReferral.getId().intValue();
		destinationProgramName = clientReferral.getProgramName();
		destinationProgramType = clientReferral.getProgramType();
		referralDate = clientReferral.getReferralDate();
		completionDate = clientReferral.getCompletionDate();
		sourceProgramName = clientReferral.getCompletionNotes();
		external = clientReferral.getNotes();
	}

	public ReferralHistoryDisplay(Referral referral) {
		isRemoteReferral = true;
		id = referral.getReferralId();

		try {
			FacilityIdIntegerCompositePk programId = new FacilityIdIntegerCompositePk();
			programId.setIntegratorFacilityId(referral.getDestinationIntegratorFacilityId());
			programId.setCaisiItemId(referral.getDestinationCaisiProgramId());

			CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(programId);
			destinationProgramName = cachedProgram.getName();
			destinationProgramType = cachedProgram.getType();			
		} catch (Exception e) {
			destinationProgramName = "N/A";
			destinationProgramType = "N/A";
			logger.error("unexpected error", e);
		}

		referralDate = referral.getReferralDate();
		
		// no completion date available yet
		// completionDate=referral.getCompletionDate();

		// completionNotes=referral.getCompletionNotes();

		// no exteral bit for remote referrals
		// external=referral.getNotes();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDestinationProgramName() {
		return (StringEscapeUtils.escapeHtml(destinationProgramName));
	}

	public void setDestinationProgramName(String destinationProgramName) {
		this.destinationProgramName = destinationProgramName;
	}

	public String getDestinationProgramType() {
		return (StringEscapeUtils.escapeHtml(destinationProgramType));
	}

	public void setDestinationProgramType(String destinationProgramType) {
		this.destinationProgramType = destinationProgramType;
	}

	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public String getReferralDateFormatted() {
		if (referralDate != null) return (DateFormatUtils.ISO_DATETIME_FORMAT.format(referralDate));
		else return ("");
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getCompletionDateFormatted() {
		if (completionDate != null) return (DateFormatUtils.ISO_DATETIME_FORMAT.format(completionDate));
		else return ("");
	}

	public String getSourceProgramName() {
		return (StringEscapeUtils.escapeHtml(sourceProgramName));
	}

	public void setSourceProgramName(String sourceProgramName) {
		this.sourceProgramName = sourceProgramName;
	}

	public String getExternal() {
		return (StringEscapeUtils.escapeHtml(external));
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public boolean isRemoteReferral() {
		return isRemoteReferral;
	}

	public void setRemoteReferral(boolean isRemoteReferral) {
		this.isRemoteReferral = isRemoteReferral;
	}

	public boolean getIsRemoteReferral() {
		return isRemoteReferral;
	}

	public void setIsRemoteReferral(boolean isRemoteReferral) {
		this.isRemoteReferral = isRemoteReferral;
	}
}
