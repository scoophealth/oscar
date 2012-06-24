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

package org.caisi.tickler.prepared.seaton.consultation;

import java.io.FileReader;

import org.apache.commons.betwixt.io.BeanReader;

public class ConsultationConfiguration {
	String filename;
	
	public ConsultationConfiguration(String filename) {
		this.filename = filename;
	}
	
	
    public ConsultationsConfigBean loadConfig() throws Exception{
        
    	FileReader xmlReader = new FileReader(filename);
        
        // Now convert this to a bean using betwixt
        // Create BeanReader
        BeanReader beanReader  = new BeanReader();
        
        // Configure the reader
        // If you're round-tripping, make sure that the configurations are compatible!
        beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        beanReader.getBindingConfiguration().setMapIDs(false);
        
        // Register beans so that betwixt knows what the xml is to be converted to
        // Since the element mapped to a PersonBean isn't called the same, 
        // need to register the path as well
        beanReader.registerBeanClass("consultations", ConsultationsConfigBean.class);
        
        // Now we parse the xml
        ConsultationsConfigBean consultations = (ConsultationsConfigBean) beanReader.parse(xmlReader);
        
        return consultations;
    }
}
