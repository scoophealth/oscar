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


package oscar.oscarLab.ca.all.parsers.OscarToOscarHl7V2;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.XPN;
import ca.uhn.hl7v2.model.v26.group.REF_I12_PROVIDER_CONTACT;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.PRD;

public final class RefI12Handler extends ChainnedMessageAdapter<REF_I12> {
	
	private static Logger logger = MiscUtils.getLogger();

	public RefI12Handler(REF_I12 hl7Message) {
	    super(hl7Message);
    }	
	
	@Override
	public String getDocName() {
		// look through provider records for the referring provider
		
		logger.debug("hl7Message.getPROVIDER_CONTACTReps()="+hl7Message.getPROVIDER_CONTACTReps());
		for (int i = 0; i < hl7Message.getPROVIDER_CONTACTReps(); i++) {
			try {
	            REF_I12_PROVIDER_CONTACT providerContact = hl7Message.getPROVIDER_CONTACT(i);

	            PRD prd = providerContact.getPRD();

	            logger.debug("prd.getProviderRole(0).getIdentifier()='"+prd.getProviderRole(0).getIdentifier()+'\'');
	            if ("RP".equals(prd.getProviderRole(0).getIdentifier().getValue())) {
	            	XPN xpn = prd.getProviderName(0);
	            	StringBuilder sb=new StringBuilder();
	            	
	            	String temp= xpn.getPrefixEgDR().getValue();
	            	if (temp!=null)
	            	{
	            		sb.append(temp);
	            		sb.append(' ');
	            	}
	            	
	            	temp=xpn.getGivenName().getValue();
	            	if (temp!=null)
	            	{
	            		sb.append(temp);
	            		sb.append(' ');
	            	}
	            	
	            	temp=xpn.getFamilyName().getSurname().getValue();
	            	if (temp!=null) sb.append(temp);
	            	
	            	String name=sb.toString();
	            	logger.debug("xpn/name="+name);
	            	return (name);
	            }
            } catch (HL7Exception e) {
            	logger.error("Unexpected error.", e);
            }
		}

		return (null);
	}

	@Override
	public String getMessageStructureType() {
		return ("REF_I12");
	}

	@Override
	public MSH getMsh() {
		return (hl7Message.getMSH());
	}

	@Override
	public PID getPid() {
		return (hl7Message.getPID());
	}

	@Override
    public String getFillerOrderNumber() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getEncounterId() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getRadiologistInfo() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getNteForOBX(int i, int j) {
	    // TODO Auto-generated method stub
	    return null;
    }
	
	@Override
    public String getNteForPID(){
		// TODO Auto-generated method stub
	    return null;
    }

}
