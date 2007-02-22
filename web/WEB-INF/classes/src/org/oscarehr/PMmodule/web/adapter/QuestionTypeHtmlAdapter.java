package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public class QuestionTypeHtmlAdapter extends BaseIntakeNodeHtmlAdapter {
	
	public QuestionTypeHtmlAdapter(int indent, IntakeInstance instance, IntakeNode node) {
	    super(indent, instance, node);
    }

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		// Question
		indent(preBuilder).append("<tr>").append(EOL);
		beginTag();
		
		for (int i = 0; i < node.getQuestionLevel(); i++) {
			indent(preBuilder).append("<td class=\"intakeEmptyCell\"></td>").append(EOL);
        }
		
		indent(preBuilder).append("<td class=\"intakeQuestionCell\" colspan=\"").append(node.getMaxPathLength()).append("\">").append(EOL);
		beginTag();
		
		indent(preBuilder).append(node.getLabel(0)).append(EOL);
		
		endTag();
		indent(preBuilder).append("</td>").append(EOL);
		
		endTag();
		indent(preBuilder).append("</tr>").append(EOL);

	    return preBuilder;
    }

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		return postBuilder;
	}
	
}