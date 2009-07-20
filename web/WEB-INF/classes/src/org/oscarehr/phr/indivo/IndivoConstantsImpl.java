/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * 
 *
 * Created on April 27, 2007, 4:24 PM
 */

package org.oscarehr.phr.indivo;

import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.oscarehr.phr.PHRConstants;
/**
 *
 * @author Paul
 */

public class IndivoConstantsImpl implements PHRConstants {
    
    
    /** Creates a new instance of IndivoConstantsImpl */
    public IndivoConstantsImpl() {
    }
    
    public String DOCTYPE_MEDICATION() {
        return DocumentClassificationUrns.MEDICATION;
    }
    public String DOCTYPE_MESSAGE() {
        return DocumentClassificationUrns.MESSAGE;
    }
    public String DOCTYPE_BINARYDATA() {
        return DocumentClassificationUrns.BINARYDATA;
    }
    public String DOCTYPE_ACCESSPOLICIES() {
        return org.indivo.xml.urns.DocumentClassificationUrns.ACCESS_POLICIES;
    }
    public String DOCTYPE_MEASUREMENT() {
        return DocumentClassificationUrns.VITAL;
    }
    public String DOCTYPE_ANNOTATION() {
        return DocumentClassificationUrns.ANNOTATION;
    }
}
