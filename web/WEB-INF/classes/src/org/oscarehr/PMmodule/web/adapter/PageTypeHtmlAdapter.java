package org.oscarehr.PMmodule.web.adapter;

import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;

public class PageTypeHtmlAdapter extends BaseIntakeNodeHtmlAdapter {

	public PageTypeHtmlAdapter(int indent, IntakeInstance instance, IntakeNode node) {
		super(indent, instance, node);
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPreBuilder()
	 */
	public StringBuilder getPreBuilder() {
		// Page Container
		if (isFirstChild()) {
			preBuilder.append("<div dojoType=\"TabContainer\" class=\"intakePageContainer\" >").append(EOL);
			beginTag();
		}

		// Page
		indent(preBuilder).append("<div dojoType=\"ContentPane\" label=\"").append(node.getLabel(0)).append("\" >").append(EOL);
		beginTag();

		return preBuilder;
	}

	/**
	 * @see org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter#getPostBuilder()
	 */
	public StringBuilder getPostBuilder() {
		// Page
		endTag();
		indent(postBuilder).append("</div> <!-- End Page -->").append(EOL);

		if (isLastChild()) {
			// Page Container
			endTag();
			indent(postBuilder).append("</div> <!-- End Page Container -->").append(EOL);
		}

		return postBuilder;
	}

}