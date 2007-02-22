package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public abstract class BaseAnswerTypeHtmlAdapter extends BaseIntakeNodeHtmlAdapter {
	
	protected BaseAnswerTypeHtmlAdapter(int index, IntakeInstance instance, IntakeNode node) {
	    super(index, instance, node);
    }

	public StringBuilder getPostBuilder() {
		return postBuilder;
	}
	
	protected StringBuilder startRow(StringBuilder builder) {
		indent(builder).append("<tr>").append(EOL);
		beginTag();

		for (int i = 0; i < node.getQuestionLevel(); i++) {
			indent(builder).append("<td class=\"intakeEmptyCell\"></td>").append(EOL);
        }

		return builder;
	}
	
	protected StringBuilder startCell(StringBuilder builder) {
		indent(builder).append("<td class=\"intakeAnswerCell\" colspan=\"").append(node.getMaxPathLength()).append("\">").append(EOL);
		beginTag();
		
		return builder;
	}
	
	protected StringBuilder endCell(StringBuilder builder) {
		endTag();
		indent(builder).append("</td>").append(EOL);
		
		return builder;
	}
	
	protected StringBuilder endRow(StringBuilder builder) {
		endTag();
		indent(builder).append("</tr>").append(EOL);
		
		return builder;
	}

	protected StringBuilder setChecked(StringBuilder builder) {
		if (isEqual("on")) {
			builder.append(" checked=\"checked\"");
		}
		
		return builder;
	}
	
}