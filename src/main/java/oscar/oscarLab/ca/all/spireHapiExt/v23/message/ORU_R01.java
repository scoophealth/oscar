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
//package oscar.v23.message;
package oscar.oscarLab.ca.all.spireHapiExt.v23.message;

import ca.uhn.hl7v2.HL7Exception;
import oscar.oscarLab.ca.all.spireHapiExt.v23.segment.ZDS;
//import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ORU_R01 extends ca.uhn.hl7v2.model.v23.message.ORU_R01 {

 	/**
	 * Constructor
     *
     * We always have to have a constructor with this one argument
	 */
	public ORU_R01(ModelClassFactory factory) throws HL7Exception {
	   super(factory);
	   
       this.add(ZDS.class, false, true);
	}


    /**
     * Add an accessor for the ZDS segment
     */
    public ZDS getZDS(int rep) throws HL7Exception {
        return (ZDS) get("ZDS", rep);
    }
    
    public ZDS[] getZDSList() throws HL7Exception {
        Structure[] list = getAll("ZDS");
        ZDS[] zdsSegments = new ZDS[list.length];
        
        for (int i=0; i < list.length; i++)
			zdsSegments[i] = (ZDS) list[i];
			
		return zdsSegments;
    }
    
    public int getNumZDSSegments() throws HL7Exception {
		return currentReps("ZDS");
	}

}
