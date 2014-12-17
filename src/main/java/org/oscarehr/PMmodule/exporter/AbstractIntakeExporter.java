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

package org.oscarehr.PMmodule.exporter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
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

	private static final String ANSWER_DECLINED = "declined";

	private GenericIntakeManager genericIntakeManager;
	private List<Integer> clients;
	private Integer programId;
	private Integer facilityId;
	private String fieldsFile;

	private IValidator validator;

	protected Intake intake;
	protected List<DATISField> fields;

	private static final Logger log = Logger.getLogger(AbstractIntakeExporter.class);

	public String export() throws ExportException {
		StringBuilder buf = new StringBuilder();

		initExport();

		log.debug("Number of Clients to export: " + clients.size());

		for(Integer clientId : clients) {
			getIntakeForClient(clientId);
			buf.append(exportData() + "\n");
		}

		return buf.toString();
	}

	protected abstract String exportData() throws ExportException;

	private void initExport() throws ExportException {
		try {
			fields = new ArrayList<DATISField>();
			loadFields(fieldsFile);

			log.debug("Fields loaded from file " + fieldsFile);

			if(null == clients || clients.isEmpty()) {
				// Get all clients for the specified facility...
				log.debug("Fetching clients for facility ID: " + facilityId);
				clients = genericIntakeManager.getIntakeClientsByFacilityId(facilityId);
			}

		} catch(Exception t) {
			throw new ExportException(t);
		}
	}

	private void getIntakeForClient(Integer clientId) throws ExportException {
		try {
			intake = genericIntakeManager.getMostRecentQuickIntake(clientId, facilityId);
		} catch(Exception t) {
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
				field.setName(fieldNode.getAttributes().getNamedItem(FIELD_NAME).getNodeValue().toUpperCase());
				field.setType(fieldNode.getAttributes().getNamedItem(FIELD_TYPE).getNodeValue());
				field.setColumnPosition(Integer.parseInt(fieldNode.getAttributes().getNamedItem(FIELD_COLPOS).getNodeValue()));
				field.setMaxSize(Integer.parseInt(fieldNode.getAttributes().getNamedItem(FIELD_MAXSIZE).getNodeValue()));
				if(null != fieldNode.getAttributes().getNamedItem(FIELD_DESC)) {
					field.setDescription(fieldNode.getAttributes().getNamedItem(FIELD_DESC).getNodeValue());
				}

				if(DATISType.DATETIME.equals(field.getType())) {
					if(null != fieldNode.getAttributes().getNamedItem(FIELD_DATEFORMAT)) {
						field.setDateFormat(fieldNode.getAttributes().getNamedItem(FIELD_DATEFORMAT).getNodeValue());
					}
				}

				if(null != fieldNode.getAttributes().getNamedItem(FIELD_QUESTION)) {
					field.setQuestion(fieldNode.getAttributes().getNamedItem(FIELD_QUESTION).getNodeValue().toUpperCase());
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

	public List<Integer> getClients() {
		return clients;
	}

	public void setClients(List<Integer> clients) {
		this.clients = clients;
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

	protected void writeData(StringBuilder buf, IntakeAnswer ans, DATISField found) {
		String value = getFieldValue(ans, found);

		buf.append(value);
	}

	protected void writeKeyValue(StringBuilder buf, IntakeAnswer ans, DATISField found) {
		String value = getFieldValue(ans, found);

		buf.append(found.getName() + " = " + value + "\n");
	}

	protected void writeCSV(StringBuilder buf, IntakeAnswer ans, DATISField field) {
		String value = getFieldValue(ans, field);

		if(validator != null) {
			value = validator.validate(field, value);
		}

		buf.append(value + ",");
	}

	private String getFieldValue(IntakeAnswer ans, DATISField field) {
		String value = ans.getValue();

		if(value.equalsIgnoreCase(ANSWER_DECLINED)) {
			value = " ";
		}

		if(value.length() > field.getMaxSize()) {
			value = value.substring(0, field.getMaxSize());
		} else {
			value = StringUtils.rightPad(value, field.getMaxSize(), ' ');
		}

		return value;
	}

	public IValidator getValidator() {
		return validator;
	}

	public void setValidator(IValidator validator) {
		this.validator = validator;
	}

}
