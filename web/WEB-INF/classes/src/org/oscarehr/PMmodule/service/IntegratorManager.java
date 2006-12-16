package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;

public interface IntegratorManager {
	
	public static final short DATATYPE_CLIENT = 	1;
	public static final short DATATYPE_PROVIDER = 	2;
	
	public boolean isActive();
	
	public boolean isEnabled();
	public boolean isRegistered();
	public void refresh();
	public String register(Agency agencyInfo, String key);
	public List getAgencies();
	public Agency getAgency(String id);
	
	public long getLocalAgencyId();
	/* program */
	public void updateProgramData(List programs);
	public List searchPrograms(Program criteria);
	public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException;
	
	/* jms */
	public void sendReferral(Long agencyId,ClientReferral referral);
	
	/* client linking */
	public Demographic getDemographic(long agencyId, long demographicNo) throws IntegratorException;
	public Demographic[] matchClient(Demographic client) throws IntegratorException;
	public void saveClient(Demographic client) throws IntegratorException;
	public void mergeClient(Demographic localClient, long remoteAgency, long remoteClientId) throws IntegratorException;
	public long getLocalClientId(long agencyId, long demographicNo) throws IntegratorException;
	
	/* refreshing */
	public void refreshPrograms(List programs) throws IntegratorException;
	public void refreshAdmissions(List admissions) throws IntegratorException;
	public void refreshProviders(List providers) throws IntegratorException;
	public void refreshReferrals(List referrals) throws IntegratorException;
	public void refreshClients(List clients) throws IntegratorException;
	
	public List getCurrentAdmissions(long clientId) throws IntegratorException;
	public List getCurrentReferrals(long clientId) throws IntegratorException;
	
	public boolean notifyUpdate(short dataType, String id);
	public Demographic getClient(String demographicNo);
	
	public String getIntegratorVersion();
}
