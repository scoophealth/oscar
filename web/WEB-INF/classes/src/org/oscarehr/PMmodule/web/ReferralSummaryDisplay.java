/**
 * 
 */
package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.util.MiscUtils;

public class ReferralSummaryDisplay {
	public static final Comparator<ReferralSummaryDisplay> REFERRAL_DATE_COMPARATOR=new Comparator<ReferralSummaryDisplay>() {
		public int compare(ReferralSummaryDisplay arg0, ReferralSummaryDisplay arg1) {
			return(arg1.referralDate.compareTo(arg0.referralDate));
		}
	};

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

	private String programName;
	private String programType;
	private String referralDate;
	private String referringProvider;
	private int daysInQueue;

	public ReferralSummaryDisplay(ClientReferral clientReferral) {
		programName = "local / "+clientReferral.getProgramName();
		programType = clientReferral.getProgramType();
		referralDate = dateFormatter.format(clientReferral.getReferralDate());
		referringProvider = clientReferral.getProviderFormattedName();
		daysInQueue = MiscUtils.calculateDayDifference(clientReferral.getReferralDate(), new Date());
	}

	public ReferralSummaryDisplay(Referral referral) throws MalformedURLException {
		CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(referral.getDestinationIntegratorFacilityId());
		
		FacilityIdIntegerCompositePk remoteProgramPk = new FacilityIdIntegerCompositePk();
		remoteProgramPk.setIntegratorFacilityId(referral.getDestinationIntegratorFacilityId());
		remoteProgramPk.setCaisiItemId(referral.getDestinationCaisiProgramId());
		CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(remoteProgramPk);
		
		programName = cachedFacility.getName()+" / "+cachedProgram.getName();
		programType = cachedProgram.getType();

		referralDate = dateFormatter.format(referral.getReferralDate());
		daysInQueue = MiscUtils.calculateDayDifference(referral.getReferralDate(), new Date());

		FacilityIdStringCompositePk remoteProviderPk = new FacilityIdStringCompositePk();
		remoteProviderPk.setIntegratorFacilityId(referral.getSourceIntegratorFacilityId());
		remoteProviderPk.setCaisiItemId(referral.getSourceCaisiProviderId());
		CachedProvider cachedProvider = CaisiIntegratorManager.getProvider(remoteProviderPk);
		referringProvider = cachedProvider.getLastName()+", "+cachedProvider.getFirstName();
	}

	public String getProgramName() {
		return programName;
	}

	public String getProgramType() {
		return programType;
	}

	public String getReferralDate() {
		return referralDate;
	}

	public String getReferringProvider() {
		return referringProvider;
	}

	public int getDaysInQueue() {
		return daysInQueue;
	}

}