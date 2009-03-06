/*
* 
* Copyright (c) 2001-2009. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
package org.oscarehr.PMmodule.exporter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractIntakeExporter {
	private static final String FIELD_FIELD = "field";
	private static final String FIELD_DESC = "desc";
	private static final String FIELD_DATEFORMAT = "dateformat";
	private static final String FIELD_MAXSIZE = "maxsize";
	private static final String FIELD_COLPOS = "colpos";
	private static final String FIELD_TYPE = "type";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_QUESTION = "question";
	
	private GenericIntakeManager genericIntakeManager;
	private Integer clientId;
	private Integer programId;
	private Integer facilityId;
	private String fieldsFile;
	
	protected Intake intake;
	protected List<DATISField> fields;
	
	public String export() throws ExportException {
		initExport();
		
		return exportData();
	}
	
	protected abstract String exportData() throws ExportException;
	
	private void initExport() throws ExportException {
		try {
			fields = new ArrayList<DATISField>();
			loadFields(fieldsFile);
			
			intake = genericIntakeManager.getMostRecentQuickIntake(clientId, facilityId);
		} catch(Throwable t) {
			throw new ExportException(t);
		}
	}

	private void loadFields(String fieldsFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		builder = factory.newDocumentBuilder();
    	document = builder.parse(this.getClass().getResourceAsStream(fieldsFile));
    	
    	loadData(document);
	}

	private void loadData(Document document) throws Exception {
		Element fieldsE = document.getDocumentElement();
		
		DATISField field = null;
		
		NodeList nodes = fieldsE.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++) {
			field = new DATISField();
			Node fieldNode = nodes.item(i);
			if(fieldNode.getNodeName().equals(FIELD_FIELD)) {
				field.setName(fieldNode.getAttributes().getNamedItem(FIELD_NAME).getNodeValue());
				field.setType(fieldNode.getAttributes().getNamedItem(FIELD_TYPE).getNodeValue());
				field.setColumnPosition(Integer.parseInt(fieldNode.getAttributes().getNamedItem(FIELD_COLPOS).getNodeValue()));
				field.setMaxSize(Integer.parseInt(fieldNode.getAttributes().getNamedItem(FIELD_MAXSIZE).getNodeValue()));
				if(null != fieldNode.getAttributes().getNamedItem(FIELD_DESC)) {
					field.setDescription(fieldNode.getAttributes().getNamedItem(FIELD_DESC).getNodeValue());
				}
				
				if(DATISType.DATETIME.getValue().equalsIgnoreCase(field.getType())) {
					if(null != fieldNode.getAttributes().getNamedItem(FIELD_DATEFORMAT)) {
						field.setDateFormat(fieldNode.getAttributes().getNamedItem(FIELD_DATEFORMAT).getNodeValue());
					}
				}
				
				if(null != fieldNode.getAttributes().getNamedItem(FIELD_QUESTION)) {
					field.setQuestion(fieldNode.getAttributes().getNamedItem(FIELD_QUESTION).getNodeValue());
				}
				
				fields.add(field);
			}
		}
	}

	public GenericIntakeManager getGenericIntakeManager() {
		return genericIntakeManager;
	}

	public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
		this.genericIntakeManager = genericIntakeManager;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public String getFieldsFile() {
		return fieldsFile;
	}

	public void setFieldsFile(String fieldsFile) {
		this.fieldsFile = fieldsFile;
	}
	
}
