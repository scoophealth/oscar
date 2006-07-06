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
