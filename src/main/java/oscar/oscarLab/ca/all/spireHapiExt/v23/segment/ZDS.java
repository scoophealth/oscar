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
//package oscar.v23.segment;
package oscar.oscarLab.ca.all.spireHapiExt.v23.segment;

import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v23.datatype.SI;
import ca.uhn.hl7v2.model.v23.datatype.CN;
import ca.uhn.hl7v2.model.v23.datatype.TS;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * ZDS custom segment
 *
 * Note that custom segments extend from {@link AbstractSegment}
 */
public class ZDS extends AbstractSegment {

	Logger logger = Logger.getLogger(ZDS.class);

    /**
     * Adding a serial UID is always a good idea, but optional
     */
    private static final long serialVersionUID = 1;

    /**
     * Custom segments need a constructor with exactly these arguments
     */
    public ZDS(Group parent, ModelClassFactory modelClassFactory) throws HL7Exception {
        super(parent, modelClassFactory);

        Message message = getMessage();

        // For each in the segment, call this.add()

        // ZDS-1 - Action Code
        this.add(SI.class, true, 1, 12, new Object[]{message}, "Action Code");

        // ZDS-2 - Provider
        this.add(CN.class, false, 1, 60, new Object[]{message}, "Provider");
        
        // ZDS-3 - Action Date and Time
        this.add(TS.class, false, 1, 60, new Object[]{message}, "Action Date and Time");
        
        // ZDS-4 - Action Status
        this.add(SI.class, false, 1, 12, new Object[]{message}, "Action Status");

    }

    /**
     * This method must be overridden. The easiest way is just to return null.
     */
    protected Type createNewTypeWithoutReflection(int field) {
        return null;
    }


    /**
     * Create an accessor for each field
     */
    public SI getActionCode() throws HL7Exception {
        return (SI) super.getField(1, 0); // 1 - field num( numbered from 1)
    }

    /**
     * Create an accessor for each field
     */
    public CN getProvider() throws HL7Exception {
        return (CN) super.getField(2, 0); // 1=field num(numbered from 1) 0=repetition(numbered from 0)
    }
    
    /**
     * Create an accessor for each field
     */
    public TS getDateAndTime() throws HL7Exception {
        return (TS) super.getField(3, 0); // 1=field num(numbered from 1) 0=repetition(numbered from 0)
    }
    
    /**
     * Create an accessor for each field
     */
    public SI getActionStatus() throws HL7Exception {
        return (SI) super.getField(4, 0); // 1=field num(numbered from 1) 0=repetition(numbered from 0)
    }
    
    public String toString() {
		String text = "";
		try {
			text += super.getField(1, 0).encode() + " | ";
			text += super.getField(2, 0).encode() + " | ";
			text += super.getField(3, 0).encode() + " | ";
			text += super.getField(4, 0).encode() + " | ";
		} catch (Exception e) {
			logger.error("Error converting ZDS segment to string: " + e.toString());
		}
		return text;
	}

}
