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
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.XCN;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.NTE;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.ROL;

public final class OruR01Handler extends ChainnedMessageAdapter<ORU_R01> {
	
	private static Logger logger = MiscUtils.getLogger();
	
	public OruR01Handler(ORU_R01 hl7Message) {
	    super(hl7Message);
    }	
	
	private ORU_R01_ORDER_OBSERVATION getOrderObservation() throws HL7Exception
	{
		return(hl7Message.getPATIENT_RESULT(0).getORDER_OBSERVATION(0));
	}
	
	@Override
	public String getDocName() {
		// look through provider records for the referring provider
		
		try {
	        ORU_R01_ORDER_OBSERVATION orderObservation=getOrderObservation();
	        
	        for (int i=0; i<orderObservation.getROLReps(); i++)
	        {
	        	ROL rol=orderObservation.getROL(i);
	        	if (DataTypeUtils.ACTION_ROLE_SENDER.equals(rol.getRoleROL().getIdentifier().getValue()))
	        	{
	        		XCN xcn=rol.getRolePerson(0);
	            	StringBuilder sb=new StringBuilder();
	            	
	            	String temp= xcn.getPrefixEgDR().getValue();
	            	if (temp!=null)
	            	{
	            		sb.append(temp);
	            		sb.append(' ');
	            	}
	            	
	            	temp=xcn.getGivenName().getValue();
	            	if (temp!=null)
	            	{
	            		sb.append(temp);
	            		sb.append(' ');
	            	}
	            	
	            	temp=xcn.getFamilyName().getSurname().getValue();
	            	if (temp!=null) sb.append(temp);
	            	
	            	String name=sb.toString();
	            	logger.debug("xcn/name="+name);
	            	return (name);
	        	}
	        }
	        
        } catch (HL7Exception e) {
	        logger.error("Unexpected error", e);
        }
        
        return(null);
	}

	@Override
	public String getMessageStructureType() {
        try {
            ORU_R01_ORDER_OBSERVATION orderObservation=getOrderObservation();
            for (int i=0; i<orderObservation.getNTEReps(); i++)
            {
		        NTE nte=orderObservation.getNTE(i);
		        if (nte.getCommentType().getText().getValue()!=null && nte.getCommentType().getText().getValue().length()>0)
		        {
		        	return("ORU_R01:"+nte.getCommentType().getText().getValue());
		        }
            }
        } catch (HL7Exception e) {
	        logger.error("Unexpected error", e);
        }
        
        return(null);
	}

	@Override
	public MSH getMsh() {
		return (hl7Message.getMSH());
	}

	@Override
	public PID getPid() {
		return (hl7Message.getPATIENT_RESULT().getPATIENT().getPID());
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
