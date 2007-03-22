package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;

public class HtmlAdapterFactory {

	public IntakeNodeHtmlAdapter getHtmlAdapter(int indent, IntakeNode node, Intake intake) {
		IntakeNodeHtmlAdapter htmlAdapter = null;

		if (node.isIntake()) {
			htmlAdapter = new IntakeHtmlAdapter(indent, node);
		} else if (node.isPage()) {
			htmlAdapter = new PageHtmlAdapter(indent, node);
		} else if (node.isSection()) {
			htmlAdapter = new SectionHtmlAdapter(indent, node);
		} else if (node.isQuestion()) {
			htmlAdapter = new QuestionHtmlAdapter(indent, node);
		} else if (node.isAnswerCompound()) {
			htmlAdapter = new AnswerCompoundHtmlAdapter(indent, node);
		} else if (node.isAnswerChoice()) {
			htmlAdapter = new AnswerScalarChoiceHtmlAdapter(indent, node, intake);
		} else if (node.isAnswerText()) {
			htmlAdapter = new AnswerScalarTextHtmlAdapter(indent, node, intake);
		} else if (node.isAnswerNote()) {
			htmlAdapter = new AnswerScalarNoteHtmlAdapter(indent, node, intake);
		} else {
			throw new IllegalStateException("No html adapter for type: " + node.getType());
		}

		return htmlAdapter;
	}
	
}