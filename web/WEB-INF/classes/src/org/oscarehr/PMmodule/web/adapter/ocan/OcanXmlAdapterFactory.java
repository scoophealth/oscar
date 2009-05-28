package org.oscarehr.PMmodule.web.adapter.ocan;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;

public class OcanXmlAdapterFactory {

	public IntakeNodeHtmlAdapter getOcanClientXmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter xmlAdapter = null;

		if (node.isIntake()) {
			xmlAdapter = new IntakeOcanClientXmlAdapter(indent, node);
		} else if (node.isPage()) {
			xmlAdapter = new PageOcanClientXmlAdapter(indent, node);
		} else if (node.isSection()) {
			xmlAdapter = new SectionOcanClientXmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			xmlAdapter = new QuestionOcanClientXmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			xmlAdapter = new AnswerCompoundOcanXmlAdapter(indent, node);
		} else if (node.isAnswerChoice()) {
			xmlAdapter = new AnswerScalarChoiceOcanClientXmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			xmlAdapter = new AnswerScalarTextOcanClientXmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			xmlAdapter = new AnswerScalarNoteOcanClientXmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No ocan xml adapter for type: " + node.getType());
		}

		return xmlAdapter;
	}
	
	public IntakeNodeHtmlAdapter getOcanStaffXmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter xmlAdapter = null;

		if (node.isIntake()) {
			xmlAdapter = new IntakeOcanStaffXmlAdapter(indent, node);
		} else if (node.isPage()) {
			xmlAdapter = new PageOcanStaffXmlAdapter(indent, node);
		} else if (node.isSection()) {
			xmlAdapter = new SectionOcanStaffXmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			xmlAdapter = new QuestionOcanStaffXmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			xmlAdapter = new AnswerCompoundOcanXmlAdapter(indent, node);
		} else if (node.isAnswerChoice()) {
			xmlAdapter = new AnswerScalarChoiceOcanStaffXmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			xmlAdapter = new AnswerScalarTextOcanStaffXmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			xmlAdapter = new AnswerScalarNoteOcanStaffXmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No ocan xml adapter for type: " + node.getType());
		}

		return xmlAdapter;
	}
}